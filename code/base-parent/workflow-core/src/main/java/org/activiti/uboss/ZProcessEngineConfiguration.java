package org.activiti.uboss;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;

public class ZProcessEngineConfiguration extends StandaloneInMemProcessEngineConfiguration {

	private ZRuntimeServiceImpl zRuntimeService;
	
	public ZProcessEngineConfiguration(){
		super(ZExtendsionUtil.ExtendsionKey_Z);
	}
	
	protected void init() {
		super.init();
		zRuntimeService = new ZRuntimeServiceImpl(super.getRuntimeService());
	}

	@Override
	public RuntimeService getRuntimeService() {
		return zRuntimeService;
	}
}
