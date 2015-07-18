package cn.com.weixunyun.child.model.dao;

public abstract class AbstractMapperProvider {

	protected String pageCount(String sql) {
		return "select count(t_.*) from (" + sql + ") t_";
	}

	protected String page(String sql, String order, boolean asc) {
		return sql + " order by " + order + " " + (asc ? "asc" : "desc") + " limit #{rows} offset #{offset} ";
	}

	protected String page(String sql, String order) {
		return sql + " order by " + order + " desc limit #{rows} offset #{offset} ";
	}

	protected String page(String sql) {
		return sql + " limit #{rows} offset #{offset} ";
	}
}
