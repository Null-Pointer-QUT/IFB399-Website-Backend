package com.pj.project4sp.public4mapper;

/**
 * 以lambda表达式回滚事务的辅助类
 * @author Runtian
 *
 */
public interface JdbcLambdaRollbackRT {
	
	/**
	 * 事务发生异常的方法 
	 * @param e
	 * @return
	 */
	public Object run(Exception e);
	
}
