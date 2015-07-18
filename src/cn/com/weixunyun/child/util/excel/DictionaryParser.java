package cn.com.weixunyun.child.util.excel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.com.weixunyun.child.model.pojo.DictionaryValue;
import cn.com.weixunyun.child.model.service.DictionaryValueService;
import cn.com.weixunyun.child.model.service.ServiceFactory;

public class DictionaryParser extends AbstractParser<String> {

	private Map<String, String> map;
	private String keys;
	
	public String exception;

	public DictionaryParser(Long schoolId, String tableName, String fieldName) {
		List<DictionaryValue> list = ServiceFactory.getService(DictionaryValueService.class).getValueList(schoolId,
				tableName, fieldName);
		
		this.map = new LinkedHashMap<String, String>();
		for (DictionaryValue d : list) {
			this.map.put(d.getName(), d.getCode());
		}
		this.keys = map.keySet().toString();
		this.exception = "必须为：" + this.keys;
	}

	@Override
	public String parse(String s, Long schoolId) throws ParserException {
		if (map.containsKey(s)) {
			return map.get(s);
		} else {
			throw new ParserException("必须为：" + keys);
		}
	}

}
