package openDart;

import Model.Company;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.regex.Pattern;

public class ExcelWrite_4 {
    //private static XSSFWorkbook workbook = new XSSFWorkbook();
    //private static XSSFSheet sheet = workbook.createSheet("Dart Companies");
    private static Sheet sheet;
    private static FileOutputStream fileOutputStream;

    public static void excelWrite(Company companyInfo, Workbook wb, int rowIndex) throws IOException {
        String[] companyData;
        String regex = "^[0-9]{10}$";   // regular expression
        boolean match = Pattern.matches(regex, companyInfo.getBizNo().replace("-",""));
        if (match){
            companyData = new String[]{companyInfo.getUniqueNo(), companyInfo.getCorpName(), companyInfo.getBizNo(), ""};
        }else{
            companyData = new String[]{companyInfo.getUniqueNo(), companyInfo.getCorpName(), companyInfo.getBizNo(), "Mismatch"};
        }

        sheet = wb.getSheet("Dart Companies");
        Row row = sheet.createRow(rowIndex);
        int columnCount = 0;
        for (String field : companyData) {
            Cell cell = row.createCell(columnCount++);
            if (field instanceof String) {
                cell.setCellValue((String) field);
            }
        }

        fileOutputStream = new FileOutputStream("./DartCompany.xlsx");
        wb.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
