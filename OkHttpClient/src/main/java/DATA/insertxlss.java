package DATA;




import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 1:向数据库中添加数据
 * @author biexiansheng
 *
 */
public class insertxlss {

    public static void main(String[] args) throws SQLException {
        try {

            ArrayList list = new ArrayList();
            List numlist= new ArrayList();
            Class.forName("com.mysql.jdbc.Driver");//加载数据库驱动
            System.out.println("加载数据库驱动成功");
            String url="jdbc:mysql://localhost:3306/xls?useUnicode=true&characterEncoding=utf8";//声明数据库test的url
            String user="root";//数据库的用户名
            String password="123";//数据库的密码
            //建立数据库连接，获得连接对象conn(抛出异常即可)
            Connection conn=DriverManager.getConnection(url, user, password);
            System.out.println("连接数据库成功");
            //生成一条mysql语句
            String sql1="select distinct 区县 from xlss";
            Statement stmt=conn.createStatement();//创建一个Statement对象
            ResultSet rs = stmt.executeQuery(sql1);
            while (rs.next()) {
                String quxian=rs.getString(1);
                list.add(rs.getString(1));
            }

            for (int i=0; i<list.size();i++){
                sql1 = "select  count(1) from xlss where 区县='"+list.get(i)+"' ";
                rs = stmt.executeQuery(sql1);
                while (rs.next()) {
                  int count=Integer.valueOf(rs.getString(1));
                  System.out.print(count+"*********");
                  for(int j=1; j<=count;j++){
                      String n = "0"+j;
                      numlist.add(n);
                      System.out.println(n);
                  }
                }
            }
            for (int i=0;i<numlist.size();i++){
                String num = String.valueOf(numlist.get(i));
                sql1="insert into xls (code) values ('"+num+"')";
              /*  try{
                    PreparedStatement st2 = conn.prepareStatement("insert into xlss (code) values (?)");
                    st2.setString(1,num);
                }catch(SQLException s){
                    s.printStackTrace();
                }*/
                stmt.executeUpdate(sql1);
            }
            conn.close();
            System.out.println("关闭数据库成功");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//


    }

}

