package cn.com.weixunyun.child.util.excel;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTimeParser extends AbstractParser<java.sql.Timestamp> {
	
	public String exception = "日期格式有误，如：2013年1月1日 1:10";

	@Override
	public java.sql.Timestamp parse(String s, Long schoolId) throws ParserException {
		try {
			String arr[] = s.split("\"");
			String str = "";
			for (int i = 0; i < arr.length; i++) {
				str = str + arr[i];
			}
			String pattern = "yyyy年MM月dd日 hh:mm";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return new java.sql.Timestamp(sdf.parse(str).getTime());
		} catch (NumberFormatException e) {
			throw new ParserException(exception);
		} catch (ParseException ex) {
			throw new ParserException(exception);
		}
	}

}
