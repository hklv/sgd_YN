package com.ztesoft.uboss.form.model;
import java.util.Date;


/**
 * @author zsmart-auto-generate
 * 
 */

public  class DynFormDto
{	//
	private long dynFormId;
	//
	private String formName;
	//
	private String formCode;
	//
	private String formTable;
	//
	private String formType;
	//
	private String formDomain;
	//
	private long userId;
	//
	private Date createTime;
	//
	private Date modifyTime;
	//
	private String formState;
	//
	private String formDesc;
	
	private Long catId;
	
	
	public Long getCatId() {
		return catId;
	}
	public void setCatId(Long catId) {
		this.catId = catId;
	}
	/**
     * @return the 
     */
   
	public long getDynFormId (){
		return dynFormId;
	}
	/**
     * @return the 
     */
   
	public String getFormName (){
		return formName;
	}
	/**
     * @return the 
     */
    
	public String getFormCode (){
		return formCode;
	}
	/**
     * @return the 
     */
   
	public String getFormTable (){
		return formTable;
	}
	/**
     * @return the 
     */
    
	public String getFormType (){
		return formType;
	}
	/**
     * @return the 
     */
   
	public String getFormDomain (){
		return formDomain;
	}
	/**
     * @return the 
     */

	public long getUserId (){
		return userId;
	}
    /**
     * @return the 
     */
   
    public Date getCreateTime() {
		return createTime;
	}
    /**
     * @return the 
     */
   
    public Date getModifyTime() {
		return modifyTime;
	}
	/**
     * @return the 
     */
   
	public String getFormState (){
		return formState;
	}
	/*
	* @param id the id to set
	*/
	public void setDynFormId(long dynFormId){
		this.dynFormId=dynFormId;
	}
	/*
	* @param formName the formName to set
	*/
	public void setFormName(String formName){
		this.formName=formName;
	}
	/*
	* @param formCode the formCode to set
	*/
	public void setFormCode(String formCode){
		this.formCode=formCode;
	}
	/*
	* @param formTable the formTable to set
	*/
	public void setFormTable(String formTable){
		this.formTable=formTable;
	}
	/*
	* @param formType the formType to set
	*/
	public void setFormType(String formType){
		this.formType=formType;
	}
	/*
	* @param formDomain the formDomain to set
	*/
	public void setFormDomain(String formDomain){
		this.formDomain=formDomain;
	}
	/*
	* @param userId the userId to set
	*/
	public void setUserId(long userId){
		this.userId=userId;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/*
	* @param formState the formState to set
	*/
	public void setFormState(String formState){
		this.formState=formState;
	}
	public String getFormDesc() {
		return formDesc;
	}
	public void setFormDesc(String formDesc) {
		this.formDesc = formDesc;
	}
	
}