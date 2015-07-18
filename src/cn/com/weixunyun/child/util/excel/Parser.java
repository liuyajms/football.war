package cn.com.weixunyun.child.util.excel;

public interface Parser<T> {

	T parse(String s, Long schoolId) throws ParserException;
}
