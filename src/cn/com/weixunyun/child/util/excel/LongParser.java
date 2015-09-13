package cn.com.weixunyun.child.util.excel;

public class LongParser extends AbstractParser<Long> {
	
	public String exception = "必须为整数";

	@Override
	public Long parse(String s) throws ParserException {
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			throw new ParserException(exception);
		}
	}

}
