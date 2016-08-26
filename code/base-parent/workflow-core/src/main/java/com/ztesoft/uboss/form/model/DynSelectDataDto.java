package com.ztesoft.uboss.form.model;
import com.ztesoft.zsmart.core.utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author zsmart-auto-generate
 * 
 */

public  class DynSelectDataDto
{	//
	private long id;
	//
	private String glsm;
	//
	private String glbz;
	//
	private String glbm;
	//
	private String state;
	//
	private Date createDate;
	//
	private long userId;
	//
	private byte[] glcontent;
	//
	private String glcontentStr;
	
	
	/**
     * @return the 
     */
    
	public long getId (){
		return id;
	}
	/**
     * @return the 
     */
   
	public String getGlsm (){
		return glsm;
	}
	/**
     * @return the 
     */
    
	public String getGlbz (){
		return glbz;
	}
	/**
     * @return the 
     */
    
	public String getGlbm (){
		return glbm;
	}
	/**
     * @return the 
     */
   
	public String getState (){
		return state;
	}
	/**
     * @return the 
     */
   
	public Date getCreateDate (){
		return createDate;
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
	* @param glsm the glsm to set
	*/
	public void setGlsm(String glsm){
		this.glsm=glsm;
	}
	/*
	* @param glbz the glbz to set
	*/
	public void setGlbz(String glbz){
		this.glbz=glbz;
	}
	/*
	* @param glbm the glbm to set
	*/
	public void setGlbm(String glbm){
		this.glbm=glbm;
	}
	/*
	* @param state the state to set
	*/
	public void setState(String state){
		this.state=state;
	}
	/*
	* @param createDate the createDate to set
	*/
	public void setCreateDate(Date createDate){
		this.createDate=createDate;
	}
	/*
	* @param userId the userId to set
	*/
	public void setUserId(long userId){
		this.userId=userId;
	}
	public byte[] getGlcontent() {
		return glcontent;
	}
	public void setGlcontent(byte[] glcontent) {
		this.glcontent = glcontent;
		if(glcontent != null)
		{
			try{
			this.glcontentStr = new String(glcontent,"GBK");
//			System.out.println(this.designXmlStr);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	public String getGlcontentStr() {
		return glcontentStr;
	}
	public void setGlcontentStr(String glcontentStr) {
		this.glcontentStr = glcontentStr;
		if(StringUtil.isNotEmpty(glcontentStr))
		{
			try {
				this.glcontent = glcontentStr.getBytes("GBK");
				System.out.println("designXml:"+new String(this.glcontent));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}