package com.ztesoft.uboss.form.model;
import com.ztesoft.zsmart.core.utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author zsmart-auto-generate
 * 
 */
 

public  class DynFormVerDto
{	//
	private long id;
	//
	private long dynFormId;
	//
	private byte[] designXml;
	//
	private byte[] designHtml;
	//
	private String version;
	//
	private String verState;
	//
	private Date createTime;
	//
	private long userId;
	//
	private String designXmlStr;
	//
	private String designHtmlStr;
	
	private String tableName;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
     * @return the 
     */
 
	public long getId (){
		return id;
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
 
	public byte[] getDesignXml() {
		return designXml;
	}
	
	/**
     * @return the 
     */
 
	public byte[] getDesignHtml() {
		return designHtml;
	}
	
	/**
     * @return the 
     */
 
	public String getVersion (){
		return version;
	}
    
	public String getVerState() {
		return verState;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	/**
     * @return the 
     */
 
	public long getUserId (){
		return userId;
	}
	/*
	* @param id the id to set
	*/
	public void setId(long id){
		this.id=id;
	}
	/*
	* @param dynFormId the dynFormId to set
	*/
	public void setDynFormId(long dynFormId){
		this.dynFormId=dynFormId;
	}
	public void setDesignXml(byte[] designXml) {
		this.designXml = designXml;
		if(designXml != null)
		{
			try{
			this.designXmlStr = new String(designXml,"GBK");
//			System.out.println(this.designXmlStr);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	public void setDesignHtml(byte[] designHtml) {
		this.designHtml = designHtml;
		if(designHtml != null)
		{
			try{
				this.designHtmlStr = new String(designHtml,"GBK");
//				System.out.println(this.designXmlStr);
				}catch(Exception ex){
					ex.printStackTrace();
				}
           
		}
	}
	/*
	* @param version the version to set
	*/
	public void setVersion(String version){
		this.version=version;
	}
	public void setVerState(String verState) {
		this.verState = verState;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/*
	* @param userId the userId to set
	*/
	public void setUserId(long userId){
		this.userId=userId;
	}
	public String getDesignXmlStr() {
		return designXmlStr;
	}
	public void setDesignXmlStr(String designXmlStr) {
		this.designXmlStr = designXmlStr;
		if(StringUtil.isNotEmpty(designXmlStr))
		{
			try {
				this.designXml = designXmlStr.getBytes("GBK");
//				System.out.println("designXml:"+new String(this.designXml));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public String getDesignHtmlStr() {
		return designHtmlStr;
	}
	public void setDesignHtmlStr(String designHtmlStr) {
		this.designHtmlStr = designHtmlStr;
		if(StringUtil.isNotEmpty(designHtmlStr))
		{
//			System.out.println("dto:"+designHtmlStr);
			try {
				this.designHtml = designHtmlStr.getBytes("GBK");
//				System.out.println("dto2:"+new String(this.designHtml));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}