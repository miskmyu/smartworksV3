package ifez.framework.session.service;

import java.io.Serializable;

public class UserSessionVO implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String userId;//사용자ID
    private String groupId;//그룹ID
    private String passwd;//패스워드
    private String userName;//사용자이름(로케일에 따라 KOREAN이면 한글명)
    private String userNameKo;//사용자이름한글
    private String userNameEn;//사용자이름영문
    private String locale;//지역
    private String empNo;//사번
    private String gradeCd;//직급코드
    private String gradeName;//직급명
    private String officePhone;//사무실전화번호
    private String cellularPhone;//휴대폰번호
    private String mail;//메일주소
    private String status;//상태
    private String reqLocal;//접속지역
    
    private String programUrl;//프로그램URL
    private boolean programAuth = false;//프로그램별 권한
    
    private String cryptoUserId;
    private String cryptoPasswd;
    
    private String errorCode = "";//에러코드로 body_top.jsp에서 메세지 처리
    
    
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return userId;
    }
    
    public void setGroupId(String groupId){
        this.groupId = groupId;
    }
    public String getGroupId(){
        return groupId;
    }
    
    public void setPasswd(String passwd){
        this.passwd = passwd;
    }
    public String getPasswd(){
        return passwd;
    }
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public void setUserNameKo(String userNameKo){
        this.userNameKo = userNameKo;
    }
    public String getUserNameKo(){
        return userNameKo;
    }
    
    public void setUserNameEn(String userNameEn){
        this.userNameEn = userNameEn;
    }
    public String getUserNameEn(){
        return userNameEn;
    }
    
    public void setLocale(String locale){
        this.locale = locale;
    }
    public String getLocale(){
        return locale;
    }
    
    public void setEmpNo(String empNo){
        this.empNo = empNo;
    }
    public String getEmpNo(){
        return empNo;
    }
    
    public void setGradeCd(String gradeCd){
        this.gradeCd = gradeCd;
    }
    public String getGradeCd(){
        return gradeCd;
    }
    
    public void setGradeName(String gradeName){
        this.gradeName = gradeName;
    }
    public String getGradeName(){
        return gradeName;
    }
    
    public void setOfficePhone(String officePhone){
        this.officePhone = officePhone;
    }
    public String getOfficePhone(){
        return officePhone;
    }
    
    public void setCellularPhone(String cellularPhone){
        this.cellularPhone = cellularPhone;
    }
    public String getCellularPhone(){
        return cellularPhone;
    }
    
    public void setMail(String mail){
        this.mail = mail;
    }
    public String getMail(){
        return mail;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return status;
    }
    
    public void setReqLocal(String reqLocal){
        this.reqLocal = reqLocal;
    }
    public String getReqLocal(){
        return reqLocal;
    }
    public String getProgramUrl() {
        return programUrl;
    }
    public void setProgramUrl(String programUrl) {
        this.programUrl = programUrl;
    }
    public boolean isProgramAuth() {
        return programAuth;
    }
    public void setProgramAuth(boolean programAuth) {
        this.programAuth = programAuth;
    }
    /**
     * @return the cryptoUserId
     */
    public String getCryptoUserId()
    {
        return cryptoUserId;
    }
    /**
     * @param cryptoUserId the cryptoUserId to set
     */
    public void setCryptoUserId(String cryptoUserId)
    {
        this.cryptoUserId = cryptoUserId;
    }
    /**
     * @return the cryptoPasswd
     */
    public String getCryptoPasswd()
    {
        return cryptoPasswd;
    }
    /**
     * @param cryptoPasswd the cryptoPasswd to set
     */
    public void setCryptoPasswd(String cryptoPasswd)
    {
        this.cryptoPasswd = cryptoPasswd;
    }
    /**
     * @return the errorCode
     */
    public String getErrorCode()
    {
        return errorCode;
    }
    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }
}

