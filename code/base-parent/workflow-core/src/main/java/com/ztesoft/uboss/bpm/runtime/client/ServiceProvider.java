package com.ztesoft.uboss.bpm.runtime.client;

import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.JdbcUtil;
import com.ztesoft.zsmart.core.jdbc.ds.ConnectionProvider;
import com.ztesoft.zsmart.core.jdbc.ds.ConnectionProviderFactory;
import com.ztesoft.zsmart.core.jdbc.ds.DbIdentifier;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.uboss.ZProcessEngineConfiguration;

public final class ServiceProvider {
	private ProcessEngineConfiguration engineConfiguration;
	private ProcessEngine processEngine;
	// TODO 临时的线程池
	private ThreadPoolExecutor threadPool;
	private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(
			20);
	private boolean isInitialized = false;

	public ServiceProvider() {
	}

	public synchronized void init() {
		if (isInitialized) {
			throw new IllegalStateException(
					"ServiceProvider is already initialized");
		}
		try {
			if (engineConfiguration == null) {
				engineConfiguration = buildDefaultEngineConfiguration();
			}
			if (threadPool == null) {
				threadPool = new ThreadPoolExecutor(5, 100, 60L,
						TimeUnit.SECONDS, workQueue);
			}
			processEngine = buildProcessEngine(engineConfiguration);
			isInitialized = true;
		} catch (Exception ex) {
			isInitialized = false;
			if (ex instanceof ActivitiException) {
				throw (ActivitiException) ex;
			}
			throw new ActivitiException("Fail to initialize ServiceProvider",
					ex);
		}
	}

	public void setProcessEngineConfiguration(
			ProcessEngineConfiguration engineConfig) {
		this.engineConfiguration = engineConfig;
	}

	public void setThreadPool(ThreadPoolExecutor threadPool) {
		this.threadPool = threadPool;
	}

	private ProcessEngineConfiguration buildDefaultEngineConfiguration() {
		ProcessEngineConfiguration engineConfiguration = new ZProcessEngineConfiguration();

		try {
			// 设置数据源
			DbIdentifier dbId = JdbcUtil.getDbIdentifier();
			AssertUtil.isNotNull(dbId, "DbIdentifier is null");
			ConnectionProvider connPd = ConnectionProviderFactory
					.getConnectionProvider(dbId);
			// ConnectionProvider connPd = dbId.getConnectionProvider();
			AssertUtil.isNotNull(connPd, "ConnectionProvider is null");

			DataSource ds = connPd.getDataSource();
			AssertUtil.isNotNull(ds, "DataSource is null");
			engineConfiguration.setDataSource(ds);
		} catch (Exception ex) {
			throw new ActivitiException(
					"Fail to initialize ServiceProvider:cannot get datasource for DbBackService",
					ex);
		}

		engineConfiguration.setDatabaseSchemaUpdate("false");
		engineConfiguration.setJobExecutorActivate(false);
		// engineConfiguration.setMailServerPort(5025);
		return engineConfiguration;
	}

	private ProcessEngine buildProcessEngine(
			ProcessEngineConfiguration engineConfiguration) {
		if (engineConfiguration == null) {
			return ProcessEngines.getDefaultProcessEngine();
		}
		return engineConfiguration.buildProcessEngine();
	}

	public void asynExec(FutureTask<?> task) {
		this.threadPool.execute(task);
	}

	public ProcessEngineConfiguration getProcessEngineConfiguration() {
		return engineConfiguration;
	}

	public ProcessEngineConfigurationImpl getProcessEngineConfigurationImpl() {
		return (ProcessEngineConfigurationImpl) engineConfiguration;
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	public RepositoryService getRepositoryService() {
		return processEngine.getRepositoryService();
	}

	public RuntimeService getRuntimeService() {
		return processEngine.getRuntimeService();
	}

	public TaskService getTaskService() {
		return processEngine.getTaskService();
	}

	public FormService getFormService() {
		return processEngine.getFormService();
	}

	public HistoryService getHistoryService() {
		return processEngine.getHistoryService();
	}

	/*
	 * public ThreadPoolExecutor getThreadPool(){ return this.threadPool; }
	 */

	public static void main(String[] args) throws BaseAppException,
			SQLException {
		DbIdentifier dbId = JdbcUtil.getDbIdentifier();
		ConnectionProvider connPd = ConnectionProviderFactory
				.getConnectionProvider(dbId);

		AssertUtil.isNotNull(connPd, "ConnectionProvider is null");

		DataSource ds = connPd.getDataSource();
		System.out.println(ds.getConnection());
	}
}
