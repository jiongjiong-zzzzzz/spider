package Excel;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class test {

        public static void main(String[] arg) throws Exception{
            Connection conn;
            PreparedStatement stmt;
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/excel?useUnicode=true&characterEncoding=utf8";
            String user = "root";
            String password = "123";
            String sql = "insert into xls values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            File file = new File("E:/111.xls");
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
                        stmt.setString(12,allData.get(i).getPkc());
                        stmt.setString(13,allData.get(i).getPkh());
                        stmt.setString(14,allData.get(i).getFgl());
                        stmt.setString(15,allData.get(i).getFgperson());
                        stmt.setString(16,allData.get(i).getShouru());
                        stmt.setString(17,allData.get(i).getDaidonghs());
                        stmt.setString(18,allData.get(i).getDaidongrs());

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



