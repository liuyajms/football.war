package cn.com.weixunyun.child.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.com.weixunyun.child.util.excel.Parser;
import cn.com.weixunyun.child.util.excel.ParserException;

public class ExcelParser {

	private ExcelParserColumn excelParserColumn = new ExcelParserColumn();

	public List<Map<String, Object>> redXlsx(String tableName, File file, String r, Long schoolId) throws Exception {
		List<ColumnProperties> propertiesList = null;
		if ("student".equals(tableName)) {
			propertiesList = excelParserColumn.getStudentParserList(schoolId);
		} else if ("teacher".equals(tableName)) {
			propertiesList = excelParserColumn.getTeacherParserList(schoolId);
		} else if ("classes".equals(tableName)) {
			propertiesList = excelParserColumn.getClassesParserList(schoolId);
		} else if ("studentGrow".equals(tableName)){ //学生成长记录
            propertiesList = excelParserColumn.getStudentGrowParserList(schoolId);
        } else if ("courseScore".equals(tableName)){ //学生成绩册
            propertiesList = excelParserColumn.getCourseScoreParserList(schoolId);
        } else if ("courseKnowledge".equals(tableName)){ //知识树
            propertiesList = excelParserColumn.getCourseKnowledgeParserList(schoolId);
        } else if ("courseClassroom".equals(tableName)){ //微课堂
            propertiesList = excelParserColumn.getCourseClassroomParserList(schoolId);
        } else if ("courseEvaluation".equals(tableName)){ //课堂评价
            propertiesList = excelParserColumn.getCourseEvaluationParserList(schoolId);
        } else if ("download".equals(tableName)){ //资料管理
            propertiesList = excelParserColumn.getDownloadParserList(schoolId);
        } else if ("news".equals(tableName)){ //新闻：概况等
            propertiesList = excelParserColumn.getNewsParserList(schoolId);
        } else if ("notice".equals(tableName)){ //学校公告
            propertiesList = excelParserColumn.getNoticeParserList(schoolId);
        } else if ("broadcastGrade".equals(tableName)){ //年级广播
            propertiesList = excelParserColumn.getBroadcastGradeParserList(schoolId);
        } else if ("broadcastClasses".equals(tableName)){ //班级广播
            propertiesList = excelParserColumn.getBroadcastClassesParserList(schoolId);
        } else if ("elective".equals(tableName)){ //选课安排
            propertiesList = excelParserColumn.getElectiveParserList(schoolId);
        } else if ("weiboClasses".equals(tableName)){ //班级广播
            propertiesList = excelParserColumn.getWeiboClassesParserList(schoolId);
        } else if ("star".equals(tableName)){ //班级明星
            propertiesList = excelParserColumn.getStarParserList(schoolId);
        } else if ("cook".equals(tableName)){ //食谱
            propertiesList = excelParserColumn.getCookParserList(schoolId);
        }  else if ("course".equals(tableName)){ //课程
            propertiesList = excelParserColumn.getCourseParserList(schoolId);
        } else if ("electiveStudent".equals(tableName)){ //班级广播
            propertiesList = excelParserColumn.getElectiveStudentParserList(schoolId);
        } else if ("curriculum".equals(tableName)){ //课表
            propertiesList = excelParserColumn.getCurriculumParserList(schoolId);
        } else if ("sensitive".equals(tableName)){//敏感词	
        	propertiesList = excelParserColumn.getSensitiveParserList(schoolId);
        } else if ("classesTeacher".equals(tableName)){//任课教师	
        	propertiesList = excelParserColumn.getClassesTeacherParserList(schoolId);
        }

		if (propertiesList == null) {
			throw new RuntimeException("参数错误");
		}

		List<ColumnError> errorList = new ArrayList<ColumnError>();
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			XSSFWorkbook book = new XSSFWorkbook(is);
			XSSFSheet sheet = book.getSheetAt(0);
			//System.out.println(sheet.getSheetName());
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				Map<String, Object> cellMap = new HashMap<String, Object>();
				int j = 0;
				for (ColumnProperties properties : propertiesList) {
					XSSFCell cell = null;
					if (j < row.getLastCellNum() && row.getCell(j) != null) {
						cell = row.getCell(j);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
					String fieldName = properties.getName();
					Parser<?> parser = (Parser<?>) properties.getParser();
					if (row.getCell(j) == null || cell.getStringCellValue() == null || "".equals(cell.getStringCellValue())) {
						if (!properties.isNullable()) {
							errorList.add(new ColumnError(i, j, null, "不能为空"));
						} else {
							cellMap.put(fieldName, null);
						}
					} else {
						if (parser == null) {
							cellMap.put(fieldName, cell);
						} else {
							Object o = null;
							try {
								o = parser.parse(cell.getStringCellValue(), schoolId);
							} catch (ParserException e) {
								errorList.add(new ColumnError(i, j, cell.getStringCellValue(), e.getMessage()));
							}
							cellMap.put(fieldName, o);
						}
					}
					j++;
				}
				list.add(cellMap);
			}

			if (errorList.size() > 0) {
				StringBuffer sb = new StringBuffer();
				for (ColumnError error : errorList) {
					sb.append(error.toString() + "\n");
				}
				throw new RuntimeException(sb.toString());
			}
			return list;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
