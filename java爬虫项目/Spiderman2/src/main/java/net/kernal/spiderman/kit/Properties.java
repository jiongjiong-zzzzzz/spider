package net.kernal.spiderman.kit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * HashMap Help类
 *
 * @author 赖伟威 l.weiwei@163.com 2015-12-10
 */
public class Properties extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public Properties() {
    }

    public final static Properties from(String[] args) {
        final Properties r = new Properties();
        Stream.of(args)
                .reduce((a, b) -> {
                    if (a.startsWith("-")) {
                        r.put(a, b);
                    }
                    return b;
                });

        return r;
    }

    public final static Properties from(String file) {
        final Properties r = new Properties();
        InputStream in = null;
        try {
            in = Properties.class.getResourceAsStream(file);
            java.util.Properties props = new java.util.Properties();
            props.load(in);
            for (Object key : props.keySet()) {
                r.put((String) key, props.get(key));
            }
        } catch (Throwable e) {
            //ignore
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }

        return r;
    }

    public Properties getProperties(String key) {
        Object v = this.get(key);
        if (v instanceof Map) {
            @SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) v;
            Properties props = new Properties();
            props.putAll(map);
            return props;
        }
        return null;
    }

    public List<Properties> getListProperties(String key) {
        Object vs = this.get(key);
        if (vs instanceof List) {
            @SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) vs;
            final List<Properties> r = new ArrayList<>();
            list.forEach(v -> {
                if (v instanceof Map) {
                    @SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) v;
                    Properties props = new Properties();
                    props.putAll(map);
                    r.add(props);
                }
            });
            return r;
        }
        return null;
    }


    public String getString(String key, String defaultVal) {
        try {
            Object v = get(key);
            if (v == null) return defaultVal;
            if (v instanceof Object[]) {
                Object[] nv = (Object[]) v;
                return String.valueOf(nv[0]);
            }
            return String.valueOf(v);
        } catch (Throwable e) {
            //ignore
        }
        return defaultVal;
    }

    public int getInt(String key, int defaultVal) {
        try {
            return Integer.parseInt(getString(key));
        } catch (Throwable e) {
            //ignore
        }
        return defaultVal;
    }

    public byte getByte(String key, byte defaultVal) {
        try {
            return Byte.parseByte(getString(key));
        } catch (Throwable e) {
            //ignore
        }
        return defaultVal;
    }

    public long getLong(String key, long defaultVal) {
        try {
            return Long.parseLong(getString(key));
        } catch (Throwable e) {
            //ignore
        }
        return defaultVal;
    }

    public float getFloat(String key, float defaultVal) {
        try {
            return Float.parseFloat(getString(key));
        } catch (Throwable e) {
            //ignore
        }
        return defaultVal;
    }

    public double getDouble(String key, double defaultVal) {
        try {
            return Double.parseDouble(getString(key));
        } catch (Throwable e) {

        }
        return defaultVal;
    }

    public boolean getBoolean(String key, boolean defaultVal) {
        try {
            String str = getString(key, defaultVal + "");
            return Boolean.parseBoolean("1".equals(str) ? "true" : str);
        } catch (Throwable e) {
            //ignore
        }
        return defaultVal;
    }

    public int getInt(String key) {
        try {
            return Integer.parseInt(getString(key));
        } catch (Throwable e) {
            //ignore
        }

        return 0;
    }

    public String getString(String key) {
        try {
            return getString(key, null);
        } catch (Throwable e) {

        }
        return null;
    }

    public Long getLong(String key) {
        try {
            return Long.parseLong(getString(key));
        } catch (Throwable e) {

        }
        return null;
    }

    public Float getFloat(String key) {
        try {
            return Float.parseFloat(getString(key));
        } catch (Throwable e) {

        }
        return null;
    }

    public Double getDouble(String key) {
        try {
            return Double.parseDouble(getString(key));
        } catch (Throwable e) {

        }
        return null;
    }

    public Boolean getBoolean(String key) {
        try {
            return Boolean.parseBoolean(getString(key));
        } catch (Throwable e) {

        }
        return null;
    }

    public List<String> getListString(String key) {
        return getListString(key, null, ",");
    }

    public List<String> getListString(String key, String defaultVal, String split) {
        List<String> list = new ArrayList<String>();
        Object obj = get(key);
        if (obj != null) {
            //若本身就是List 或者 Array 类型，直接返回
            if (obj instanceof List) {
                list.addAll(((List<?>) obj).stream().map((Function<Object, String>) String::valueOf).collect(Collectors.toList()));
                return list;
            }

            if (obj instanceof Object[]) {
                for (Object v : (Object[]) obj) {
                    list.add(String.valueOf(v));
                }
                return list;
            }
        }

        //否则按给定的split进行分隔变成数组返回
        String[] arr = this.getString(key, defaultVal == null ? "" : defaultVal).split(split);
        for (String s : arr) {
            list.add(s);
        }

        return list;
    }

    public Class<?> getClass(String key, Class<?> defaultValue) {
        if (!this.containsKey(key)) {
            return defaultValue;
        }

        return (Class<?>) this.get(key);
    }

}