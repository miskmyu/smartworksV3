package net.smartworks.server.service.util;

public interface IParallelProcessing{	
	public abstract Object getResult();
	public abstract Object[] getArrayResult();
	public abstract void setResult(Object result);
	public abstract void setArrayResult(Object[] arrayResult);
	public abstract void doRun() throws Exception;	
}
