package Model;

public class Company {
    private String uniqueNo;// 고유번호
    private String corpName;// 기업체명
    private String bizNo;   // 사업자번호
    private String jurirNo; // 법인번호

    public Company(String uniqueNo, String corpName, String bizNo, String jurirNo) {
        this.uniqueNo = uniqueNo;
        this.corpName = corpName;
        this.bizNo = bizNo;
        this.jurirNo = jurirNo;
    }

    public String getUniqueNo() {
        return uniqueNo;
    }

    public void setUniqueNo(String uniqueNo) {
        this.uniqueNo = uniqueNo;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getJurirNo() {
        return jurirNo;
    }

    public void setJurirNo(String jurirNo) {
        this.jurirNo = jurirNo;
    }

    @Override
    public String toString() {
        return "Company{" +
                "고유번호='" + uniqueNo + '\'' +
                ", 기업명='" + corpName + '\'' +
                ", 사업자번호='" + bizNo + '\'' +
                ", 법인번호='" + jurirNo + '\'' +
                '}';
    }
}
