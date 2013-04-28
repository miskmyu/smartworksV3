package net.smartworks.server.engine.invoker.model;

public class Invoker {

	private String sql = null;
	private boolean isFunction = false;
	private String functionName = null;
	private String[] functionArgs = null;
	private String returnType = null;
	private String[] returnColumn = null;
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public boolean isFunction() {
		return isFunction;
	}
	public void setFunction(boolean isFunction) {
		this.isFunction = isFunction;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String[] getFunctionArgs() {
		return functionArgs;
	}
	public void setFunctionArgs(String[] functionArgs) {
		this.functionArgs = functionArgs;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String[] getReturnColumn() {
		return returnColumn;
	}
	public void setReturnColumn(String[] returnColumn) {
		this.returnColumn = returnColumn;
	}
	
}
