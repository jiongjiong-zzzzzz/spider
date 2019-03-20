package Excel;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class test1 {

        public static void main(String[] arg) throws Exception{
            Connection conn;
            PreparedStatement stmt;
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/excel?useUnicode=true&characterEncoding=utf8";
            String user = "root";
            String password = "123";
            String sql = "insert into xlss values (?,?,?,?,?,?,?,?,?,?,?)";
            File file = new File("E:/22.xls");
            List<excel> allData=excelRead.readExcel(file);
            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(url, user, password);
                stmt = (PreparedStatement) conn.prepareStatement(sql);
                for (int i=0; i<allData.size();i++){
                    try{

                        stmt.setString(1,allData.get(i).getCity());
                        stmt.setString(2,allData.get(i).getQuxian());
                        stmt.setString(3,allData.get(i).getType());
                        stmt.setString(4,allData.get(i).getChanye());
                        stmt.setString(5,allData.get(i).getName());
                        stmt.setString(6,allData.get(i).getGuimo());
                        stmt.setString(7,allData.get(i).getDanwei());
                        stmt.setString(8,allData.get(i).getChanliang());
                        stmt.setString(9,allData.get(i).getCldanwei());
                        stmt.setString(10,allData.get(i).getZchanzhi());
                        stmt.setString(11,allData.get(i).getNummber());


                        stmt.executeUpdate();

                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                if(conn!=null){
                    conn.close();
                    conn = null;
                }
                if(stmt!=null){
                    stmt.close();
                }



                //stmt.setInt(1, 9958);
                //stmt.setString(2, "eagle2");
               // stmt.setString(3, "king3");
              //  stmt.setInt(4,2017);
                //stmt.executeUpdate();

            } catch (ClassNotFoundException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }

    }



