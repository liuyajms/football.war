package cn.com.weixunyun.child.util.excel;

public class PhoneParser extends AbstractParser<String> {
	
	public String exception = "必须为数字，且长度为11位";

	@Override
	public String parse(String s, Long schoolId) throws ParserException {
		if (s.length() == 11) {
			try {
				Long.parseLong(s);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new ParserException(exception);
			}
			return s;
		} else {
			throw new ParserException(exception);
		}
	}

}
