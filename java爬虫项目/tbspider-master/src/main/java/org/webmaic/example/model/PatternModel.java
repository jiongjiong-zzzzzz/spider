package org.webmaic.example.model;

public enum PatternModel {

    PARENTHESES_CONTENT("(?<=\\().*(?=\\))");



    private String value;

    PatternModel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据code获取当前的枚举对象
     */
    public static PatternModel of(String value) {
        if (value == null) {
            return null;
        }
        for (PatternModel status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
}

