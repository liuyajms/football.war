//package cn.com.weixunyun.child.util.excel;
//
//import jxl.Cell;
//
//public class UnicodeParser extends AbstractParser<String> {
//
//	private DictionaryParser parser;
//
//	public UnicodeParser() {
//		parser = new DictionaryParser("ghm_geo", "type");
//	}
//
//	@Override
//	public String parse(String regionCode, Cell[] cells, String s) throws ParserException {
//		try {
//			String typeCode = parser.parse(regionCode, cells, cells[9].getContents());
//			if (s.substring(0, 8).equals(regionCode + typeCode)) {
//				return s;
//			} else {
//				throw new ParserException();
//			}
//		} catch (ParserException e) {
//			throw new ParserException("统一号码未按照规则填写");
//		}
//	}
//
//}
