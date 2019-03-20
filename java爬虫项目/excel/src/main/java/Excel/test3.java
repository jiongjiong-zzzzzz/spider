package Excel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.text.SimpleDateFormat;
public class test3 {
    public static List<sqlziduan>   getExcel() throws Exception {
        File file = new File("C:\\Users\\guowe\\Desktop\\11.xls");
        List allData = excelRead.readExcel(file);

        List<String> oneData = new ArrayList<String>();
        List<sqlziduan> excelData = new ArrayList<sqlziduan>();
        int level_1 = 0, level_2 = 0, level_3 = 0, level_4 = 0, level_5 = 0;
        String parent_id1 = "",parent_id2 = "",parent_id3 = "",parent_id4 = "";

        int class_level = 0;
        String class_name = "";
        int sort_number = 0;
        String parent_id = "0";
        String id_paths = "0";
        System.out.println(allData.size());
        for (int i = 0; i < allData.size(); i++) {
            oneData = (List<String>) allData.get(i);

            String id = oneData.get(6);
            String keysword = oneData.get(5);
            for (int j = 0; j < oneData.size(); j++) {
                if (oneData.get(j) == "") {
                    continue;
                }
                while (oneData.get(j) != "") {
                    class_name = oneData.get(j);
                    class_level = j + 1;

                    switch (class_level) {
                        case 1:
                            level_1++;
                            level_2 = 0;
                            level_3 = 0;
                            level_4 = 0;
                            level_5 = 0;
                            sort_number = level_1;
                            break;
                        case 2:
                            level_2++;
                            level_3 = 0;
                            level_4 = 0;
                            level_5 = 0;
                            sort_number = level_2;
                            break;
                        case 3:
                            level_3++;
                            level_4 = 0;
                            level_5 = 0;
                            sort_number = level_3;
                            break;
                        case 4:
                            level_4++;
                            level_5 = 0;
                            sort_number = level_4;
                            break;
                        case 5:
                            level_5++;
                            sort_number = level_5;
                            break;
                    }


                    switch (class_level) {
                        case 1:
                            parent_id = "0";
                            id_paths = "0";
                            parent_id1 = oneData.get(6);
                            parent_id2 = null;
                            parent_id3 = null;
                            parent_id4 = null;
                            break;
                        case 2:
                            parent_id = parent_id1;
                            id_paths = parent_id1;
                            parent_id2 = oneData.get(6);
                            parent_id3 = null;
                            parent_id4 = null;
                            break;
                        case 3:
                            parent_id = parent_id2;
                            id_paths = parent_id1+","+parent_id2;
                            parent_id3 = oneData.get(6);
                            parent_id4 = null;
                            break;
                        case 4:
                            parent_id = parent_id3;
                            id_paths = parent_id1+","+parent_id2+","+parent_id3;
                            parent_id4 = oneData.get(6);
                            break;
                        case 5:
                            parent_id = parent_id4;
                            id_paths = parent_id1+","+parent_id2+","+parent_id3+","+parent_id4;

                            break;
                    }
                    sqlziduan sq = new sqlziduan();
                    sq.setClass_desc(class_name);
                    sq.setClass_level(class_level);
                    sq.setClass_name(class_name);
                    sq.setId(id);
                    sq.setId_paths(id_paths);
                    sq.setKeywords(keysword);
                    sq.setParent_id(parent_id);
                    sq.setSort_number(sort_number);
                    excelData.add(sq);
                    //System.out.println("class_name: " + class_name + "——————id: " + id + "———class_level: " + class_level + " ———parent_id:" + parent_id + " ———sort_number:" + sort_number + " ———id_paths:" + id_paths);

                    break;

                }
                break;
            }
        }
        return excelData;

    }
    public static Timestamp getTime() {
        Date date = new Date();
        Timestamp timeStamep = new Timestamp(date.getTime());
        return timeStamep;
    }
    public static void main(String[] arg) throws Exception {

        Connection conn;
        PreparedStatement stmt;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://39.105.51.244:30006/gwyy_product?useUnicode=true&characterEncoding=utf8";
        //String url = "jdbc:mysql://localhost:3306/sys?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "wef56tygg";
        String sql = "insert into product_class values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        List<sqlziduan> allData = getExcel();
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            stmt = (PreparedStatement) conn.prepareStatement(sql);
            for (int i=0; i<allData.size();i++){
                try{

                    stmt.setString(1,allData.get(i).getId());
                    stmt.setString(2,allData.get(i).getClass_name());
                    stmt.setInt(3,allData.get(i).getClass_level());
                    stmt.setString(4,allData.get(i).getParent_id());
                    stmt.setInt(5,allData.get(i).getSort_number());
                    stmt.setString(6,allData.get(i).getKeywords());
                    stmt.setString(7,allData.get(i).getId_paths());
                    stmt.setString(8,allData.get(i).getClass_name());
                    stmt.setBoolean(9,true);
                    stmt.setString(10,null);
                    stmt.setTimestamp(11,getTime());
                    stmt.setInt(12,1);
                    stmt.setTimestamp(13,null);
                    stmt.setInt(14,0);


                    stmt.executeUpdate();
                    System.out.println("时间："+getTime()+"    第"+(i+1)+"条插入成功");
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