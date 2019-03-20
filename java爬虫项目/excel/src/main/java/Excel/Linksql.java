package Excel;

import java.sql.*;
public class Linksql {

    //我么要执行创建表的DDl语句
    static String creatsql = "CREATE TABLE pepole("
            + "name varchar(10) not null,"
            + "age int(4) not null"
            + ")charset=utf8;";

    final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //指定连接数据库的url
    final static String DB_URL = "jdbc:mysql://localhost:3306/excel?useUnicode=true&characterEncoding=utf8";
    //mysql用户名
    final static String name = "root";
    //mysql密码
    final static String pwd = "123";
    public static void main(String[] args)
    {
        Connection conn = null;
        Statement stmt = null;
        try
        {
            //注册jdbc驱动
            Class.forName(JDBC_DRIVER);
            //打开连接
            System.out.println("//连接数据库");
            conn = DriverManager.getConnection(DB_URL,name,pwd);
            //执行创建表
            System.out.println("//创建表");
            stmt = conn.createStatement();
            if(0 == stmt.executeLargeUpdate(creatsql))
            {
                System.out.println("成功创建表！");
            }
            else
            {
                System.out.println("创建表失败！");
            }
            //
            stmt.close();
            conn.close();
            System.out.println("//关闭资源");
        }
        catch(Exception e)
        {
            System.out.println("创建表失败！");
            e.printStackTrace();
        }
    }
}
