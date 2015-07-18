package cn.com.weixunyun.child.util;

import java.util.ArrayList;
import java.util.List;

import cn.com.weixunyun.child.util.excel.*;

public class ExcelParserColumn {

	public List<ColumnProperties> getStudentParserList(Long schoolId) {
		List<ColumnProperties> list = new ArrayList<ColumnProperties>();
		list.add(new ColumnProperties("name", false));
		list.add(new ColumnProperties("gender", true, new DictionaryParser(schoolId, "student", "gender")));
		list.add(new ColumnProperties("birthday", true, new DateParser()));
		list.add(new ColumnProperties("code"));
		list.add(new ColumnProperties("card"));
		list.add(new ColumnProperties("address"));
		list.add(new ColumnProperties("description"));
		list.add(new ColumnProperties("parentsName"));
		list.add(new ColumnProperties("parentsMobile", true, new PhoneParser()));
		list.add(new ColumnProperties("parentsUsername"));
		list.add(new ColumnProperties("parentsType", true, new DictionaryParser(schoolId, "parents", "type")));
		list.add(new ColumnProperties("parentsPta", true, new BoolParser()));
		return list;
	}

	public List<ColumnProperties> getTeacherParserList(Long schoolId) {
		List<ColumnProperties> list = new ArrayList<ColumnProperties>();
		list.add(new ColumnProperties("name", false));
		list.add(new ColumnProperties("gender", true, new DictionaryParser(schoolId, "student", "gender")));
		list.add(new ColumnProperties("mobile", false, new PhoneParser()));
		list.add(new ColumnProperties("username", false));
		list.add(new ColumnProperties("code"));
		list.add(new ColumnProperties("card"));
		list.add(new ColumnProperties("title", true, new DictionaryParser(schoolId, "teacher", "title")));
		list.add(new ColumnProperties("email"));
		list.add(new ColumnProperties("remark"));
		list.add(new ColumnProperties("description"));
		list.add(new ColumnProperties("type", true, new BoolParser()));
		return list;
	}

	public List<ColumnProperties> getClassesParserList(Long schoolId) {
		List<ColumnProperties> list = new ArrayList<ColumnProperties>();
		list.add(new ColumnProperties("name", false));
		list.add(new ColumnProperties("year", false, new IntegerParser()));
		list.add(new ColumnProperties("num", false));
		list.add(new ColumnProperties("teacherId", true, new TeacherParser(schoolId)));
		list.add(new ColumnProperties("description"));
		return list;

	}

    public List<ColumnProperties> getStudentGrowParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("studentName",false));
        list.add(new ColumnProperties("studentNo",true));
        list.add(new ColumnProperties("name",true));
        list.add(new ColumnProperties("type",true,new DictionaryParser(schoolId,"student_grow","type")));
        list.add(new ColumnProperties("description",true));
        return  list;
    }

    public List<ColumnProperties> getCourseScoreParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("studentName",false));
        list.add(new ColumnProperties("studentNo",true));
        list.add(new ColumnProperties("type",true,new DictionaryParser(schoolId,"course_score","type")));
        list.add(new ColumnProperties("date",true, new DateParser()));
        list.add(new ColumnProperties("score",true, new DoubleParser()));
        return  list;
    }
    
    public List<ColumnProperties> getCourseKnowledgeParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("classesName",false));
        list.add(new ColumnProperties("courseName",false));
        list.add(new ColumnProperties("name",false));
        return  list;
    }
    
    public List<ColumnProperties> getCourseClassroomParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("classesName",false));
        list.add(new ColumnProperties("courseName",false));
        list.add(new ColumnProperties("name",false));
        return  list;
    }
    
    public List<ColumnProperties> getCourseEvaluationParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("classesName",false));
        list.add(new ColumnProperties("courseName",false));
        list.add(new ColumnProperties("studentName",false));
        list.add(new ColumnProperties("praise", true, new IntegerParser()));
        list.add(new ColumnProperties("accuracy", true,new DoubleParser()));
        list.add(new ColumnProperties("description"));
        return  list;
    }
    
    public List<ColumnProperties> getDownloadParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("classesName",false));
        list.add(new ColumnProperties("name",false));
        list.add(new ColumnProperties("topDays",true,new IntegerParser()));
        return  list;
    }
    
    public List<ColumnProperties> getNewsParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("title",false));
        list.add(new ColumnProperties("descriptionSummary"));
        return  list;
    }
    
    public List<ColumnProperties> getNoticeParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("description", false));
        list.add(new ColumnProperties("pushParents", true, new BoolParser()));
        list.add(new ColumnProperties("pushTeacher", true, new BoolParser()));
        return  list;
    }
    
    public List<ColumnProperties> getBroadcastGradeParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("grade", false));
        list.add(new ColumnProperties("title", false));
        list.add(new ColumnProperties("description"));
        return  list;
    }
    
    public List<ColumnProperties> getBroadcastClassesParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("classesName", false));
        list.add(new ColumnProperties("title", false));
        list.add(new ColumnProperties("description"));
        return  list;
    }
    
    public List<ColumnProperties> getElectiveParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("name", false));
        list.add(new ColumnProperties("teacherName", false));
        list.add(new ColumnProperties("date"));
        list.add(new ColumnProperties("num", true, new IntegerParser()));
        list.add(new ColumnProperties("grade"));
        list.add(new ColumnProperties("choosedCount"));
        return  list;
    }
    
    public List<ColumnProperties> getWeiboClassesParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("classesName", false));
        list.add(new ColumnProperties("description"));
        return  list;
    }
    
    public List<ColumnProperties> getStarParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("classesName", false));
        list.add(new ColumnProperties("name", false));
        return  list;
    }
    
    public List<ColumnProperties> getCookParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("name", false));
        return  list;
    }
    
    public List<ColumnProperties> getCourseParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("name", false));
        return  list;
    }
    
    public List<ColumnProperties> getElectiveStudentParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("electiveName", false));
        list.add(new ColumnProperties("studentName", false));
        list.add(new ColumnProperties("score"));
        list.add(new ColumnProperties("scoreDescription"));
        return  list;
    }
    
    public List<ColumnProperties> getCurriculumParserList(Long schoolId) {
        List<ColumnProperties> list = new ArrayList<ColumnProperties>();
        list.add(new ColumnProperties("classesName", false));
        list.add(new ColumnProperties("type", false));
        return  list;
    }

	public List<ColumnProperties> getSensitiveParserList(Long schoolId) {
		 List<ColumnProperties> list = new ArrayList<ColumnProperties>();
	     list.add(new ColumnProperties("name", false));	    
	     list.add(new ColumnProperties("name0", true));
	     list.add(new ColumnProperties("name1", true));
	     list.add(new ColumnProperties("name2", true));
	     list.add(new ColumnProperties("name3", true));
	     list.add(new ColumnProperties("name4", true));
	     list.add(new ColumnProperties("name5", true));
	     list.add(new ColumnProperties("name6", true));
	     list.add(new ColumnProperties("name7", true));
	     list.add(new ColumnProperties("available"));
	     return  list;
	}
	
	public List<ColumnProperties> getClassesTeacherParserList(Long schoolId) {
		 List<ColumnProperties> list = new ArrayList<ColumnProperties>();
	     list.add(new ColumnProperties("classesName"));	    
	     list.add(new ColumnProperties("courseId", true, new CourseParser(schoolId)));
	     list.add(new ColumnProperties("teacherId", true, new TeacherParser(schoolId)));
	     return  list;
	}
}
