package cn.com.weixunyun.child.util.excel;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateParser extends AbstractParser<java.util.Date> {
	
	public String exception = "必须为日期，格式为：2013-1-1";

	@Override
	public java.util.Date parse(String s, Long schoolId) throws ParserException {
		try {
			String arr[] = s.split("\"");
			String str = "";
			for (int i = 0; i < arr.length; i++) {
				str = str + arr[i];
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return new java.sql.Date(sdf.parse(str).getTime());
		} catch (NumberFormatException e) {
			throw new ParserException(exception);
		} catch (ParseException ex) {
			throw new ParserException(exception);
		}
	}

}
