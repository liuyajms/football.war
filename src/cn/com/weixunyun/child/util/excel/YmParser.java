package cn.com.weixunyun.child.util.excel;

public class YmParser extends AbstractParser<String> {
	
	public String exception = "格式必须为YYYYMM，如：201308";

	@Override
	public String parse(String s, Long schoolId) throws ParserException {
		if (s.length() == 6) {
			try {
				Long.parseLong(s);
			} catch (NumberFormatException e) {
				throw new ParserException(exception);
			}
			return s;
		} else {
			throw new ParserException(exception);
		}
	}

}
