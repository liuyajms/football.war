package cn.com.weixunyun.child.control;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.util.ColumnProperties;
import cn.com.weixunyun.child.util.ExcelParserColumn;
import cn.com.weixunyun.child.util.ExcelParserColumnName;
import cn.com.weixunyun.child.util.excel.*;
import org.apache.wink.common.annotations.Workspace;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/rule/{module}")
@Description("课程")
public class RuleResource extends AbstractResource {

    @GET
    @Description("表对应的导入字段约束列表")
    public List<Rule> selectRule(@CookieParam("rsessionid") String rsessionid,
                                 @PathParam("module") String module) throws Exception {
        String parser = module.substring(0, 1).toUpperCase() + module.substring(1);
        return rules(super.getAuthedSchoolId(rsessionid), parser);
    }

    public List<Rule> rules(Long schoolId, String str) throws Exception {
        List<Rule> pList = new LinkedList<Rule>();

        ExcelParserColumn excelParserColumn = new ExcelParserColumn();
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();

        try {
            list = (List<ColumnProperties>) excelParserColumn.getClass()
                    .getMethod("get" + str + "ParserList", new Class[]{Long.class})
                    .invoke(excelParserColumn, new Object[]{schoolId});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            list = (List<ColumnProperties>) excelParserColumn.getClass()
                    .getMethod("get" + str + "ParserList", new Class[]{Long.class, Long.class})
                    .invoke(excelParserColumn, new Object[]{schoolId, null});
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
                        pair.setValue(new DictionaryParser("student", "gender").exception);
                        pair.setType("数据字典");
                        pair.setTable("student");
                        pair.setField("gender");
                    } else if ("parentsType".equals(columnProperties.getName().toString())) {
                        pair.setValue(new DictionaryParser("parents", "type").exception);
                        pair.setType("数据字典");
                        pair.setTable("parents");
                        pair.setField("type");
                    } else if ("title".equals(columnProperties.getName().toString())) {
                        pair.setValue(new DictionaryParser("teacher", "title").exception);
                        pair.setType("数据字典");
                        pair.setTable("teacher");
                        pair.setField("title");
                    } else if ("type".equals(columnProperties.getName().toString()) && "course_score".equals(str)) {
                        pair.setValue(new DictionaryParser("course_score", "type").exception);
                        pair.setType("数据字典");
                        pair.setTable("course_score");
                        pair.setField("type");
                    } else if ("type".equals(columnProperties.getName().toString()) && "student_grow".equals(str)) {
                        pair.setValue(new DictionaryParser("student_grow", "type").exception);
                        pair.setType("数据字典");
                        pair.setTable("student_grow");
                        pair.setField("type");
                    }
                } else {
                    pair.setValue(null);
                    pair.setType("文本");
                }
            } else {
                pair.setValue(null);
                pair.setType("文本");
            }
            pList.add(pair);
        }
        return pList;
    }
}
