package cn.com.weixunyun.child.util.excel;

public class DoubleParser extends AbstractParser<Double> {
	
	public String exception = "必须为数值";

	@Override
	public Double parse(String s, Long schoolId) throws ParserException {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			throw new ParserException(exception);
		}
	}

}
