package DATA;




import java.sql.*;
import java.util.ArrayList;

/**
 * 1:向数据库中添加数据
 * @author biexiansheng
 *
 */
public class insert {

    public static void main(String[] args) throws SQLException {
        try {

            ArrayList list = new ArrayList();
            Class.forName("com.mysql.jdbc.Driver");//加载数据库驱动
            System.out.println("加载数据库驱动成功");
            String url="jdbc:mysql://localhost:3306/excell?useUnicode=true&characterEncoding=utf8";//声明数据库test的url
            String user="root";//数据库的用户名
            String password="123";//数据库的密码
            //建立数据库连接，获得连接对象conn(抛出异常即可)
            Connection conn=DriverManager.getConnection(url, user, password);
            System.out.println("连接数据库成功");
            //生成一条mysql语句
            String sql1="select distinct name from xls";
            Statement stmt=conn.createStatement();//创建一个Statement对象
            ResultSet rs = stmt.executeQuery(sql1);
            while (rs.next()) {
                list.add(rs.getString(1));
            }

            for (int i=0; i<list.size();i++){
                String sql = "";
                sql="CREATE TABLE if not exists `城市"+list.get(i)+"` (\n" +
                        "  `主导产业名称` varchar(255) DEFAULT NULL,\n" +
                        "  `城市名称` varchar(255) DEFAULT NULL,\n" +
                        "  `生产规模` varchar(255) DEFAULT NULL,\n" +
                        "  `总产量` varchar(255) DEFAULT NULL,\n" +
                        "  `总产值（万元）` varchar(255) DEFAULT NULL,\n" +
                        "  `参与新型经营主体数量（个）` varchar(255) DEFAULT NULL,\n" +
                        "  `覆盖贫困村（个）` varchar(255) DEFAULT NULL,\n" +
                        "  `覆盖贫困户（户）` varchar(255) DEFAULT NULL,\n" +
                        "  `覆盖率（%` varchar(255) DEFAULT NULL,\n" +
                        "  `人均收入（元）` varchar(255) DEFAULT NULL,\n" +
                        "  `覆盖人口（人）` varchar(255) DEFAULT NULL,\n" +
                        "  `户数` varchar(255) DEFAULT NULL,\n" +
                        "  `人数` varchar(255) DEFAULT NULL\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
                String sql3 = "CREATE TABLE if not exists `区县"+ list.get(i)+"` (\n" +
                        "  `主导产业名称` varchar(255) DEFAULT NULL,\n" +
                        "  `区县名称` varchar(255) DEFAULT NULL,\n" +
                        "  `生产规模` varchar(255) DEFAULT NULL,\n" +
                        "  `总产量` varchar(255) DEFAULT NULL,\n" +
                        "  `总产值（万元）` varchar(255) DEFAULT NULL,\n" +
                        "  `参与新型经营主体数量（个）` varchar(255) DEFAULT NULL,\n" +
                        "  `覆盖贫困村（个）` varchar(255) DEFAULT NULL,\n" +
                        "  `覆盖贫困户（户）` varchar(255) DEFAULT NULL,\n" +
                        "  `覆盖率（%` varchar(255) DEFAULT NULL,\n" +
                        "  `人均收入（元）` varchar(255) DEFAULT NULL,\n" +
                        "  `覆盖人口（人）` varchar(255) DEFAULT NULL,\n" +
                        "  `户数` varchar(255) DEFAULT NULL,\n" +
                        "  `人数` varchar(255) DEFAULT NULL\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
                if(0 == stmt.executeUpdate(sql))
                {
                    System.out.println("第"+i+"条****************************************");
                    System.out.println("城市"+list.get(i)+"成功创建表！");
                }
                if(0 == stmt.executeUpdate(sql3))
                {
                    System.out.println("区县"+list.get(i)+"成功创建表！");
                }

                String sql2="insert into 城市"+list.get(i)+" select name,city,sum(scguimo),sum(zcliang),sum(zchanzhi),sum(cygeshu),sum(pkcunshu),sum(fgpkcunshu)," +
                        "sum(fglv),Avg(rjshouru),sum(fgrenshu),sum(shushu),sum(renshu) from xls where name='"+list.get(i)+"' group by city;";
                stmt.executeUpdate(sql2);
                System.out.println("城市"+list.get(i)+"表数据插入成功！");

                String sql4="insert into 区县"+list.get(i)+" select name,quxian,sum(scguimo),sum(zcliang),sum(zchanzhi),sum(cygeshu),sum(pkcunshu),sum(fgpkcunshu)," +
                        "sum(fglv),Avg(rjshouru),sum(fgrenshu),sum(shushu),sum(renshu) from xls where name='"+list.get(i)+"' group by quxian;";
                stmt.executeUpdate(sql4);
                System.out.println("区县"+list.get(i)+"表数据插入成功！");

            }
            conn.close();
            System.out.println("关闭数据库成功");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//


    }

}

