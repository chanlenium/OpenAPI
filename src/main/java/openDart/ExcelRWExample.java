package openDart;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelRWExample {
    private static Workbook wb;
    private static Sheet sh;
    private static Row row;
    private static Cell cell;
    private static FileInputStream fis;
    private static FileOutputStream fos;


    public static void main(String[] args) {
        try {
            fis = new FileInputStream("./testdata.xlsx");
            wb = WorkbookFactory.create(new FileInputStream("./testdata.xlsx"));
            sh = wb.getSheet("Sheet1");
            int noOfRows = sh.getLastRowNum();
            System.out.println(noOfRows);
            for(int i = 0; i <= noOfRows; i++){
                System.out.println(sh.getRow(i).getCell(0));
            }

            row = sh.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue("QAV");
            System.out.println(cell.getStringCellValue());

            fos = new FileOutputStream("./testdata.xlsx");
            wb.write(fos);
            fos.flush();
            fos.close();
            System.out.println("Done");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
