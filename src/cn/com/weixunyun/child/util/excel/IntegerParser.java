package cn.com.weixunyun.child.util.excel;

public class IntegerParser extends AbstractParser<Integer> {
	
	public String exception = "必须为整数";

	@Override
	public Integer parse(String s) throws ParserException {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new ParserException(exception);
		}
	}

}
