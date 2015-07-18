package cn.com.weixunyun.child.util.excel;

public class BoolParser extends AbstractParser<Boolean> {
	
	public String exception = "必须为：是/否";

	@Override
	public Boolean parse(String s, Long schoolId) throws ParserException {
		try {
			if ("是".equals(s)) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			throw new ParserException(exception);
		}
	}

}
