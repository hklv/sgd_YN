package com.ztesoft.uboss.bpm.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztesoft.zsmart.core.utils.StringUtil;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.service.DynamicDict;


public class RuleUtil {

	 private static Map<String, KnowledgeBase> knowledgeBaseCache = new HashMap<String, KnowledgeBase>(); 
	
	 public static Collection<Object> execRule(String ruleCode, List<Object> params, DynamicDict global) throws BaseAppException{
		 
		Collection<Object> outputVariables = new ArrayList<Object>(); 
		

		KnowledgeBase knowledgeBase =  knowledgeBaseCache.get(ruleCode);
		
		if (knowledgeBase==null){
			
			//load from db
			RuleInfo ruleInfo = new RuleInfo(ruleCode);
			ruleInfo.build();
			
			if(ruleInfo.getRuleBytes() != null)
			{
				KnowledgeBuilder knowledgeBuilder =KnowledgeBuilderFactory.newKnowledgeBuilder();
		        Resource droolsResource = ResourceFactory.newByteArrayResource(ruleInfo.getRuleBytes());
		        knowledgeBuilder.add(droolsResource, ruleInfo.getResourceType());
		        knowledgeBase=knowledgeBuilder.newKnowledgeBase();
				knowledgeBaseCache.put(ruleCode, knowledgeBase);
			}
			else
			{
				ExceptionHandler.publish("rule code can not found: ruleCode=" + ruleCode);
			}
		}
		
		StatefulKnowledgeSession ksession = knowledgeBase.newStatefulKnowledgeSession();
		ksession.setGlobal("globalDict", global);

		for(Object param:params)
		{
			ksession.insert(param);
		}
		
		ksession.fireAllRules();

		Collection<Object> ruleOutputObjects = ksession.getObjects();
		if (ruleOutputObjects != null && ruleOutputObjects.size() > 0) {
			
			for (Object object : ruleOutputObjects) {
				outputVariables.add(object);
			}
		}
		
		ksession.dispose();
	
		return outputVariables;
	}
	 
	public static void updateRuleCache(String ruleCode) throws BaseAppException{
		
		if(knowledgeBaseCache.get(ruleCode) == null){
			
			return;
		}
		
		RuleInfo ruleInfo = new RuleInfo(ruleCode);
		ruleInfo.build();
		
		KnowledgeBuilder knowledgeBuilder =KnowledgeBuilderFactory.newKnowledgeBuilder();
        Resource droolsResource = ResourceFactory.newByteArrayResource(ruleInfo.getRuleBytes());
        knowledgeBuilder.add(droolsResource, ruleInfo.getResourceType());
        KnowledgeBase knowledgeBase=knowledgeBuilder.newKnowledgeBase();
		knowledgeBaseCache.put(ruleCode, knowledgeBase);
	}
	
	public static void validateRuleGrammer(String ruleStr, String ruleType) throws BaseAppException{
		
		if(StringUtil.isEmpty(ruleStr)){
			
			return;
		}
		
		KnowledgeBuilder knowledgeBuilder =KnowledgeBuilderFactory.newKnowledgeBuilder();
        Resource droolsResource;
		try {
			droolsResource = ResourceFactory.newByteArrayResource(ruleStr.getBytes("UTF-8"));
			knowledgeBuilder.add(droolsResource, RuleInfo.getRuleResourceType(ruleType));
	        knowledgeBuilder.newKnowledgeBase();
		} catch (Exception e) {
			
			if(knowledgeBuilder.hasErrors()){
				ExceptionHandler.publish(knowledgeBuilder.getErrors().toString(), e);
			}else{
				ExceptionHandler.publish("validateRuleGrammer error", e);
			}
			
		}
	}
	
	
}

class RuleInfo
{
	private byte[] ruleBytes;
	
	private ResourceType resourceType;
	
	private String ruleCode;
	
	public byte[] getRuleBytes() {
		return ruleBytes;
	}

	public void setRuleBytes(byte[] ruleBytes) {
		this.ruleBytes = ruleBytes;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	
	public RuleInfo(String ruleCode)
	{
		this.ruleCode = ruleCode;
	}
	
	public static ResourceType getRuleResourceType(String type){
		
		//default
		ResourceType resourceType = ResourceType.DRL;
		
		if("R".equals(type))
		{
			resourceType = ResourceType.DRL;
		}
		else if("T".equals(type))
		{
			resourceType = ResourceType.DTABLE;
		}
		else if("F".equals(type))
		{
			resourceType = ResourceType.DRF;
		}
		
		return resourceType;
	}
	
	public void build() throws BaseAppException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.TYPE, A.SCRIPT_DETAIL FROM tfm_rules A WHERE A.CODE = ?");

		try
		{
			Connection conn = SessionContext.currentSession().getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, ruleCode);
			
			rs = ps.executeQuery();
			if (rs.next()) {
				String type =  rs.getString(1);
				
				resourceType = getRuleResourceType(type);
				
				ruleBytes = rs.getBytes(2);
			}
		}
		catch (Exception e) {
			ExceptionHandler.publish("ruleInfo build error", e);
		}
		finally
		{
			if(rs != null)
			{
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rs=null;
			}
			
			if(ps != null)
			{
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ps=null;
			}
		}
	}
}