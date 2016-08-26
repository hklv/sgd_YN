/**
 * Copyright 2010 ZTEsoft Inc. All Rights Reserved.
 *
 * This software is the proprietary information of ZTEsoft Inc.
 * Use is subject to license terms.
 * 
 * $Tracker List
 * 
 * $TaskId: $ $Date: 9:24:36 AM (May 9, 2008) $comments: create 
 * $TaskId: $ $Date: 3:56:36 PM (SEP 13, 2010) $comments: upgrade jvm to jvm1.5 
 *  
 *  
 */
package com.ztesoft.uboss.bpm.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import com.ztesoft.zsmart.core.log.LoggingManager;
import com.ztesoft.zsmart.core.log.params.FormatArr;
import com.ztesoft.zsmart.core.log.params.LoggingParameter;
import com.ztesoft.zsmart.core.log.params.Msg;
import com.ztesoft.zsmart.core.log.params.MsgT;
import com.ztesoft.zsmart.core.utils.logging.InternalLogger;
import com.ztesoft.zsmart.core.utils.logging.InternalLoggerFactory;

/**
 * 日志封装类
 * 
 * from v7.x
 * 
 */

public final class ULogger {

	public final static String ROOT_LOGGER_NAME = "ROOT";

	public final static LoggingManager loggingManager = new LoggingManager(
			false);

	public final InternalLogger logger;

	public String name = "";

	public final static String FQCN = ULogger.class.getName();

	public final static ConcurrentHashMap<String, ULogger> loggerMap = new ConcurrentHashMap<String, ULogger>();

	public final static InternalLogger traceLogger = InternalLoggerFactory
			.getInstance("TraceLog");

	ULogger(InternalLogger logger, String name) {
		this.logger = logger;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static ULogger getLogger(String name) {

		ULogger ubossLogger = loggerMap.get(name);
		if (ubossLogger == null) {
            ubossLogger = new ULogger(InternalLoggerFactory.getInstance(name, ULogger.class.getName()), name);
			loggerMap.remove(name, ubossLogger);
			loggerMap.putIfAbsent(name, ubossLogger);
		}
		return ubossLogger;
	}

	public static ULogger getLogger(Class clazz) {
		return getLogger(clazz.getName());
	}

	private void trace(LoggingParameter lp) {

		if (lp.checkTrace()) {
			traceLogger.error(lp.toString());
		}
	}

	private void trace(LoggingParameter lp, Throwable t) {

		if (lp.checkTrace()) {
			traceLogger.error(lp.toString(), t);
		}
	}

	public boolean isDebugEnabled() {
		if (logger == null)
			return false;
		return logger.isDebugEnabled();
	}

	public void debug(Object msg) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.FINER,
				msg);
		if (isDebugEnabled()) {
			logger.debug(lp.toString());
		}
		trace(lp);
	}

	public void debug(Throwable t) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.FINER, t);
		if (isDebugEnabled()) {
			logger.debug(lp.toString(), t);
		}
		trace(lp, t);
	}

	public void debug(String format, Object... args) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.FINER,
				format, args);
		if (isDebugEnabled()) {
			logger.debug(lp.toString());
		}
		trace(lp);
	}

	public void debug(String msg, Throwable t) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.FINER,
				msg, t);
		if (isDebugEnabled()) {
			logger.debug(lp.toString(), t);
		}
		trace(lp, t);
	}

	public boolean isInfoEnabled() {
		if (logger == null)
			return false;
		return logger.isInfoEnabled();
	}

	public void info(Object msg) {
		LoggingParameter lp = loggingManager
				.doStrategy(logger, Level.FINE, msg);
		if (isInfoEnabled()) {
			logger.info(lp.toString());
		}
		trace(lp);
	}

	public void info(Throwable t) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.FINE, t);
		if (isInfoEnabled()) {
			logger.info(lp.toString(), t);
		}
		trace(lp, t);
	}

	public void info(String format, Object... args) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.FINE,
				format, args);
		if (isInfoEnabled()) {
			logger.info(lp.toString());
		}
		trace(lp);
	}

	public void info(String msg, Throwable t) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.FINE,
				msg, t);
		if (isInfoEnabled()) {
			logger.info(lp.toString(), t);
		}
		trace(lp, t);
	}

	public boolean isWarnEnabled() {
		if (logger == null)
			return false;
		return logger.isWarnEnabled();
	}

	public void warn(Object msg) {
		LoggingParameter lp = loggingManager
				.doStrategy(logger, Level.INFO, msg);

		if (isWarnEnabled()) {
			logger.warn(lp.toString());
		}
		trace(lp);
	}

	public void warn(String format, Object... args) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.INFO,
				format, args);
		if (isWarnEnabled()) {
			logger.warn(lp.toString());
		}
		trace(lp);
	}

	public void warn(String msg, Throwable t) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.INFO,
				msg, t);

		if (isWarnEnabled()) {
			logger.warn(lp.toString(), t);
		}
		trace(lp, t);
	}

	public void warn(Throwable t) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.INFO, t);

		if (isWarnEnabled()) {
			logger.warn("", t);
		}

		trace(lp, t);
	}

	public boolean isErrorEnabled() {
		if (logger == null)
			return false;
		return logger.isErrorEnabled();
	}

	public void error(Object msg) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.WARNING,
				msg);
		if (isErrorEnabled()) {
			logger.error(lp.toString());
		}
		trace(lp);
	}

	public void error(String format, Object... args) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.WARNING,
				format, args);
		if (isErrorEnabled()) {
			logger.error(lp.toString());
		}
		trace(lp);
	}

	public void error(String msg, Throwable t) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.WARNING,
				msg, t);
		if (isErrorEnabled()) {
			logger.error(lp.toString(), t);
		}
		trace(lp, t);
	}

	public void error(Throwable t) {
		LoggingParameter lp = loggingManager.doStrategy(logger, Level.WARNING,
				t);
		if (isErrorEnabled()) {
			logger.error("", t);
		}
		trace(lp, t);
	}

	/**
	 * 向网管告警的日志输出方式
	 * 
	 * @param alert
	 *            传递为 true，则必定向网管告警
	 * 
	 * @param msg
	 */
	public void alert(boolean alert, String msg) {
		LoggingParameter param = new Msg(logger, Level.WARNING, msg);
		if (alert) {
			param.setNetManLog();
		}
		param = loggingManager.doStrategy1(param);
		logger.error(param.toString());
		trace(param);
	}

	/**
	 * 向网管告警的日志输出方式
	 * 
	 * @param alert
	 *            传递为 true，则必定向网管告警
	 * 
	 * @param msg
	 * @param t
	 */
	public void alert(boolean alert, String msg, Throwable t) {
		LoggingParameter param = new MsgT(logger, Level.WARNING, msg, t);
		if (alert) {
			param.setNetManLog();
		}
		param = loggingManager.doStrategy1(param);
		logger.error(param.toString(), t);
		trace(param, t);
	}

	/**
	 * 向网管告警的日志输出方式
	 * 
	 * @param alert
	 *            传递为 true，则必定向网管告警
	 * 
	 * @param msg
	 * @param alert
	 */
	public void alert(boolean alert, String msg, Object... args) {
		LoggingParameter param = new FormatArr(logger, Level.WARNING, msg, args);
		if (alert) {
			param.setNetManLog();
		}
		param = loggingManager.doStrategy1(param);
		logger.error(param.toString());
		trace(param);
	}

	/**
	 * 输出提示日志的方式，无论何种级别输出都不会进行网管告警
	 * 
	 * @param msg
	 *            日志级别
	 * @param msg
	 */
	public void tips(String msg) {
		LoggingParameter param = new Msg(logger, Level.INFO, msg);
		param.setTipsLog();
		logger.warn(param.toString());
		trace(param);
	}

	/**
	 * 输出提示日志的方式，无论何种级别输出都不会进行网管告警
	 * 
	 * @param msg  t
	 *            日志级别
	 * @param msg
	 * @param t
	 */
	public void tips(String msg, Throwable t) {
		LoggingParameter param = new MsgT(logger, Level.INFO, msg, t);
		param.setTipsLog();
		logger.warn(param.toString(), t);
		trace(param, t);
	}

	/**
	 * 输出提示日志的方式，无论何种级别输出都不会进行网管告警
	 * 
	 * @param level
	 *            日志级别
	 * @param format
	 * @param argArray
	 */
	public void tips(String format, Object... argArray) {
		LoggingParameter param = new FormatArr(logger, Level.INFO, format,
				argArray);
		param.setTipsLog();
		logger.warn(param.toString());
		trace(param);
	}

	public static void main(String args[]) {
		try {

		} catch (Exception ex) {

		}
	}
}
