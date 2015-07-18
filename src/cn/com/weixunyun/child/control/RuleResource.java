package cn.com.weixunyun.child.control;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.wink.common.annotations.Workspace;

import cn.com.weixunyun.child.Description;
import cn.com.weixunyun.child.model.bean.Rule;
import cn.com.weixunyun.child.util.ColumnProperties;
import cn.com.weixunyun.child.util.ExcelParserColumn;
import cn.com.weixunyun.child.util.ExcelParserColumnName;
import cn.com.weixunyun.child.util.excel.BoolParser;
import cn.com.weixunyun.child.util.excel.DateParser;
import cn.com.weixunyun.child.util.excel.DateTimeParser;
import cn.com.weixunyun.child.util.excel.DictionaryParser;
import cn.com.weixunyun.child.util.excel.DoubleParser;
import cn.com.weixunyun.child.util.excel.IntegerParser;
import cn.com.weixunyun.child.util.excel.LongParser;
import cn.com.weixunyun.child.util.excel.PhoneParser;
import cn.com.weixunyun.child.util.excel.YmParser;

@Produces(MediaType.APPLICATION_JSON)
@Workspace(workspaceTitle = "Workspace Title", collectionTitle = "Collection Title")
@Path("/rule/{module}")
@Description("课程")
public class RuleResource extends AbstractResource {

	@GET
	@Description("表对应的导入字段约束列表")
	public List<Rule> selectRule(@CookieParam("rsessionid") String rsessionid, @PathParam("module") String module) {
		System.out.println("-------------------------------");
		System.out.println(module);
		System.out.println();
		return rules(super.getAuthedSchoolId(rsessionid), module);
	}

    public List<Rule> rules(Long schoolId, String str) {
        List<Rule> pList = new LinkedList<Rule>();

        ExcelParserColumn excelParserColumn = new ExcelParserColumn();
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        if ("student".equals(str)) {
            list = excelParserColumn.getStudentParserList(schoolId);
        } else if ("teacher".equals(str)) {
            list = excelParserColumn.getTeacherParserList(schoolId);
        } else if ("classes".equals(str)) {
            list = excelParserColumn.getClassesParserList(schoolId);
        } else if ("student_grow".equals(str)) {
            list = excelParserColumn.getStudentGrowParserList(schoolId);
        } else if ("course_score".equals(str)) {
            list = excelParserColumn.getCourseScoreParserList(schoolId);
        } else if ("course_knowledge".equals(str)) {
            list = excelParserColumn.getCourseKnowledgeParserList(schoolId);
        } else if ("course_classroom".equals(str)) {
            list = excelParserColumn.getCourseClassroomParserList(schoolId);
        } else if ("course_evaluation".equals(str)) {
            list = excelParserColumn.getCourseEvaluationParserList(schoolId);
        } else if ("download".equals(str)) {
            list = excelParserColumn.getDownloadParserList(schoolId);
        } else if ("news".equals(str)) {
            list = excelParserColumn.getNewsParserList(schoolId);
        } else if ("notice".equals(str)) {
            list = excelParserColumn.getNoticeParserList(schoolId);
        } else if ("broadcastGrade".equals(str)) {
            list = excelParserColumn.getBroadcastGradeParserList(schoolId);
        } else if ("broadcastClasses".equals(str)) {
            list = excelParserColumn.getBroadcastClassesParserList(schoolId);
        } else if ("elective".equals(str)) {
            list = excelParserColumn.getElectiveParserList(schoolId);
        } else if ("weiboClasses".equals(str)) {
            list = excelParserColumn.getWeiboClassesParserList(schoolId);
        } else if ("star".equals(str)) {
            list = excelParserColumn.getStarParserList(schoolId);
        } else if ("cook".equals(str)) {
            list = excelParserColumn.getCookParserList(schoolId);
        } else if ("course".equals(str)) {
            list = excelParserColumn.getCourseParserList(schoolId);
        } else if ("electiveStudent".equals(str)) {
            list = excelParserColumn.getElectiveStudentParserList(schoolId);
        } else if ("curriculum".equals(str)) {
            list = excelParserColumn.getCurriculumParserList(schoolId);
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
