package net.smartworks.server.engine.invoker.manager;

import java.util.Map;

import net.smartworks.server.engine.invoker.model.Invoker;

public interface IInvokerManager {

	public Map invoke(Invoker invoker) throws Exception;
	
}
