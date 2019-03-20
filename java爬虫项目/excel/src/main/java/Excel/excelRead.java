package Excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import jxl.Cell;
import Excel.MysqlCon;

public class excelRead {
    public static void main(String[] args) throws Exception{

        // 读取Excel文件
        File file = new File("C:\\Users\\guowe\\Desktop\\aa.xls");
        List<excel> list = excelRead.readExcel(file);


    }


    public  static List<excel> readExcel(File file) throws Exception {

        // 创建输入流，读取Excel
        InputStream is = new FileInputStream(file.getAbsolutePath());
        // jxl提供的Workbook类
        Workbook wb = Workbook.getWorkbook(is);
        // 只有一个sheet,直接处理
        //创建一个Sheet对象
        Sheet sheet = wb.getSheet(0);
        // 得到所有的行数
        int rows = sheet.getRows();
        // 所有的数据
        List allData = new ArrayList();
        // 越过第一行 它是列名称
        for (int j = 1; j < rows-1; j++) {

            List<String> oneData = new ArrayList<String>();

            // 得到每一行的单元格的数据
            Cell[] cells = sheet.getRow(j);
            for (int k = 0; k < cells.length; k++) {
                String t=cells[k].getContents().trim();
                if(t == null || t.equals(" ")){
                    t="无";
                }
                oneData.add(t);
               //System.out.println(cells[k].getContents().trim());
            }
            // 存储每一条数据
            //System.out.println(oneData);
            allData.add(oneData);
            // 打印出每一条数据
            //System.out.println(oneData);

        }
        return allData;

    }

}
