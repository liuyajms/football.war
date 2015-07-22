package cn.com.weixunyun.child.util;


import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.util.excel.*;

import java.util.*;

public class Rules {

    public static void main(String[] args) {
        System.out.println(new IntegerParser().exception);
        new Rules().columnRules(6568L, "teacher");
    }

    public Map<String, String> columnRules(Long schoolId, String str) {
        Map<String, String> map = new LinkedHashMap<String, String>();

        ExcelParserColumn excelParserColumn = new ExcelParserColumn();
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        if ("student".equals(str)) {
            list = excelParserColumn.getStudentParserList(schoolId);
        } else if ("teacher".equals(str)) {
            list = excelParserColumn.getTeacherParserList(schoolId);
        }

        for (ColumnProperties columnProperties : list) {
            System.out.println(columnProperties.getName());
            if (columnProperties.getParser() != null) {
                if (columnProperties.getParser().toString().contains("IntegerParser")) {
                    map.put(ExcelParserColumnName.map.get(str).get(columnProperties.getName()), new IntegerParser().exception);
                } else if (columnProperties.getParser().toString().contains("BoolParser")) {
                    map.put(ExcelParserColumnName.map.get(str).get(columnProperties.getName()), new BoolParser().exception);
                } else if (columnProperties.getParser().toString().contains("DateParser")) {
                    map.put(ExcelParserColumnName.map.get(str).get(columnProperties.getName()), new DateParser().exception);
                } else if (columnProperties.getParser().toString().contains("DateTimeParser")) {
                    map.put(ExcelParserColumnName.map.get(str).get(columnProperties.getName()), new DateTimeParser().exception);
                } else if (columnProperties.getParser().toString().contains("PhoneParser")) {
                    map.put(ExcelParserColumnName.map.get(str).get(columnProperties.getName()), new PhoneParser().exception);
                } else if (columnProperties.getParser().toString().contains("LongParser")) {
                    map.put(ExcelParserColumnName.map.get(str).get(columnProperties.getName()), new LongParser().exception);
                } else if (columnProperties.getParser().toString().contains("DoubleParser")) {
                    map.put(ExcelParserColumnName.map.get(str).get(columnProperties.getName()), new DoubleParser().exception);
                } else if (columnProperties.getParser().toString().contains("YmParser")) {
                    map.put(ExcelParserColumnName.map.get(str).get(columnProperties.getName()), new YmParser().exception);
                } else if (columnProperties.getParser().toString().contains("DictionaryParser")) {
                    if ("gender".equals(columnProperties.getName().toString())) {
                        map.put(ExcelParserColumnName.map.get(str).get(columnProperties.getName()), new DictionaryParser(schoolId, "student", "gender").exception);
                    } else if ("parentsType".equals(columnProperties.getName().toString())) {
                        map.put(ExcelParserColumnName.map.get(str).get(columnProperties.getName()), new DictionaryParser(schoolId, "parents", "type").exception);
                    } else if ("title".equals(columnProperties.getName().toString())) {
                        map.put(ExcelParserColumnName.map.get(str).get(columnProperties.getName()), new DictionaryParser(schoolId, "teacher", "title").exception);
                    }
                }
            } else {
                map.put(ExcelParserColumnName.map.get(str).get(columnProperties.getName()), "");
            }
        }
        System.out.println(map);
        return map;
    }

    public List<Rule> rules(Long schoolId, String str) {
        List<Rule> pList = new LinkedList<Rule>();

        ExcelParserColumn excelParserColumn = new ExcelParserColumn();
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        if ("student".equals(str)) {
            //TODO
            list = excelParserColumn.getStudentParserList(schoolId);
        }

        Rule pair = null;
        for (ColumnProperties columnProperties : list) {
            pair = new Rule();
            pair.setName(ExcelParserColumnName.map.get(str).get(columnProperties.getName()));
            if (columnProperties.getParser() != null) {
                if (columnProperties.getParser().toString().contains("IntegerParser")) {
                    pair.setValue(new IntegerParser().exception);
                    pair.setType("数值");
                } else if (columnProperties.getParser().toString().contains("BoolParser")) {
                    pair.setValue(new BoolParser().exception);
                    pair.setType("文本");
                } else if (columnProperties.getParser().toString().contains("DateParser")) {
                    pair.setValue(new DateParser().exception);
                    pair.setType("日期");
                } else if (columnProperties.getParser().toString().contains("DateTimeParser")) {
                    pair.setValue(new DateTimeParser().exception);
                    pair.setType("时间");
                } else if (columnProperties.getParser().toString().contains("PhoneParser")) {
                    pair.setValue(new PhoneParser().exception);
                    pair.setType("数值");
                } else if (columnProperties.getParser().toString().contains("LongParser")) {
                    pair.setValue(new LongParser().exception);
                    pair.setType("数值");
                } else if (columnProperties.getParser().toString().contains("DoubleParser")) {
                    pair.setValue(new DoubleParser().exception);
                    pair.setType("数值");
                } else if (columnProperties.getParser().toString().contains("YmParser")) {
                    pair.setValue(new YmParser().exception);
                    pair.setType("文本");
                } else if (columnProperties.getParser().toString().contains("DictionaryParser")) {
                    if ("gender".equals(columnProperties.getName().toString())) {
                        pair.setValue(new DictionaryParser(schoolId, "student", "gender").exception);
                        pair.setType("数据字典");
                        pair.setTable("student");
                        pair.setField("gender");
                    } else if ("parentsType".equals(columnProperties.getName().toString())) {
                        pair.setValue(new DictionaryParser(schoolId, "parents", "type").exception);
                        pair.setType("数据字典");
                        pair.setTable("parents");
                        pair.setField("type");
                    } else if ("title".equals(columnProperties.getName().toString())) {
                        pair.setValue(new DictionaryParser(schoolId, "teacher", "title").exception);
                        pair.setType("数据字典");
                        pair.setTable("teacher");
                        pair.setField("title");
                    } else if ("type".equals(columnProperties.getName().toString()) && "course_score".equals(str)) {
                        pair.setValue(new DictionaryParser(schoolId, "course_score", "type").exception);
                        pair.setType("数据字典");
                        pair.setTable("course_score");
                        pair.setField("type");
                    } else if ("type".equals(columnProperties.getName().toString()) && "student_grow".equals(str)) {
                        pair.setValue(new DictionaryParser(schoolId, "student_grow", "type").exception);
                        pair.setType("数据字典");
                        pair.setTable("student_grow");
                        pair.setField("type");
                    }
                } else if (columnProperties.getParser().toString().contains("TeacherParser")) {
                    pair.setValue(null);
                    pair.setType("文本");
                } else if (columnProperties.getParser().toString().contains("CourseParser")) {
                    pair.setValue(null);
                    pair.setType("文本");
                }
            } else {
                pair.setValue(null);
                pair.setType("文本");
            }
            pList.add(pair);
        }
        //System.out.println(pList);
        return pList;
    }
}
