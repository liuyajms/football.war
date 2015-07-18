package cn.com.weixunyun.child.util;

import cn.com.weixunyun.child.util.excel.Parser;

public class ColumnProperties {
	String name;
	boolean nullable;
	Parser<?> parser;

	ColumnProperties() {
		this(null, true, null);
	}

	ColumnProperties(String name) {
		this(name, true, null);
	}

	ColumnProperties(String name, boolean nullable) {
		this(name, nullable, null);
	}

	ColumnProperties(String name, boolean nullable, Parser<?> parser) {
		this.name = name;
		this.nullable = nullable;
		this.parser = parser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public Parser<?> getParser() {
		return parser;
	}

	public void setParser(Parser<?> parser) {
		this.parser = parser;
	}
}
