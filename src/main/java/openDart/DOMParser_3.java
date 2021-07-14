package openDart;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Model.Company;
import Utility.SSLTrust;
import org.apache.poi.ss.usermodel.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMParser_3 {
    static final String SERVICEKEY = "9bd7d00ee1eca94f1facac3c3486130a5b4eb0cf";    // service key
    private static Workbook workbook;
    private static Sheet sheet;
    private static FileInputStream fileInputStream;

    private static void extractBizrNo(NodeList corpCodeList, NodeList corpNameList, Workbook wb, int nextRowIndex) throws IOException {
        System.out.println("corpCodeList length = " + corpCodeList.getLength()); // Total number of record for "고유번호"
        for (int i = 0; i < 500; i++) {
            SSLTrust.sslTrustAllCerts();    // 보안 우회
            StringBuilder urlBuilder = new StringBuilder("https://opendart.fss.or.kr/api/company.xml");
            try {
                urlBuilder.append(
                        "?" + URLEncoder.encode("crtfc_key", "UTF-8") + "=" + URLEncoder.encode(SERVICEKEY, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("corp_code", "UTF-8") + "="
                        + URLEncoder.encode(corpCodeList.item(i).getTextContent(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            //System.out.println("conn.getResponseCode() = " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                rd = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();

                // xml을 파싱해주는 객체를 생성
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder;
                try {
                    documentBuilder = factory.newDocumentBuilder(); // Convert xml string to InputStream
                    InputStream is = new ByteArrayInputStream(sb.toString().getBytes());
                    Document doc = documentBuilder.parse(is);   // 파싱 시작
                    Element element = doc.getDocumentElement(); // 최상위 노드 찾기

                    if (element.getElementsByTagName("bizr_no").item(0) == null) {
                        Company companyInfo = new Company(corpCodeList.item(i).getTextContent(), corpNameList.item(i).getTextContent(),
                                "NULL", element.getElementsByTagName("jurir_no").item(0).getTextContent());
                        System.out.println(companyInfo);
                        ExcelWrite_4.excelWrite(companyInfo, wb, nextRowIndex++);
                    } else {
                        Company companyInfo = new Company(corpCodeList.item(i).getTextContent(), corpNameList.item(i).getTextContent(),
                                element.getElementsByTagName("bizr_no").item(0).getTextContent().replace("-",""),
                                element.getElementsByTagName("jurir_no").item(0).getTextContent());
                        System.out.println(companyInfo);
                        ExcelWrite_4.excelWrite(companyInfo, wb, nextRowIndex++);
                    }
                } catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                continue;
            }
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // XML 문서 파싱
        Path currentRelativePath = Paths.get("");
        String currentAbsoultePath = currentRelativePath.toAbsolutePath().toString();   // absolute path 

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  // build factory
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        System.out.println("currentAbsoultePath = " + currentAbsoultePath);
        Document document = documentBuilder.parse(String.valueOf(new File(currentAbsoultePath + "/CORPCODE.xml").toURI()));

        Element root = document.getDocumentElement();   // get a root
        NodeList corpCodeList = root.getElementsByTagName("corp_code");   // 원하는 태그 데이터 찾아오기
        NodeList corpNameList = root.getElementsByTagName("corp_name");   // 원하는 태그 데이터 찾아오기
        //System.out.println("uniqueNum = " + uniqueCodeList.item(0).getTextContent());
        //System.out.println("uniqueName = " + corpNameList.item(1).getTextContent());

        fileInputStream = new FileInputStream("./DartCompany.xlsx");
        workbook = WorkbookFactory.create(fileInputStream);
        sheet = workbook.getSheet("Dart Companies");
        int noOfRows = sheet.getLastRowNum();
        System.out.println("noOfRows = " + noOfRows);
        for(int i = 0; i <= noOfRows; i++){
            System.out.println(sheet.getRow(i).getCell(0));
        }

        // "CORPCODE.xml"에서 추출된 corp_code(고유번호)값에 해당하는 사업자번호를 찾기위해 함수 호출
        extractBizrNo(corpCodeList, corpNameList, workbook, ++noOfRows);
    }
}