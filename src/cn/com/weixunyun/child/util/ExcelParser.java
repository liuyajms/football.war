package cn.com.weixunyun.child.util;

import cn.com.weixunyun.child.util.excel.Parser;
import cn.com.weixunyun.child.util.excel.ParserException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelParser {

    private ExcelParserColumn excelParserColumn = new ExcelParserColumn();

    public List<Map<String, Object>> redXlsx(String tableName, File file, String r, Long schoolId) throws Exception {
        return redXlsx(tableName, file, 0, schoolId);
    }

    public List<Map<String, Object>> redXlsx(String tableName, File file, int sheetIndex, Long schoolId)
            throws Exception {
        List<ColumnProperties> propertiesList = null;


        try {
            propertiesList = (List<ColumnProperties>) excelParserColumn.getClass()
                    .getMethod("get" + tableName + "ParserList", new Class[]{Long.class})
                    .invoke(excelParserColumn, new Object[]{schoolId});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return redXlsx(propertiesList, file, sheetIndex, schoolId);
    }

    public List<Map<String, Object>> redXlsx(List<ColumnProperties> propertiesList, File file, int sheetIndex,
                                             Long schoolId) throws Exception {
        if (propertiesList == null) {
            throw new RuntimeException("参数错误");
        }

        List<ColumnError> errorList = new ArrayList<ColumnError>();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            XSSFWorkbook book = new XSSFWorkbook(is);
            XSSFSheet sheet = book.getSheetAt(sheetIndex);
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
                    if (cell == null || cell.getStringCellValue() == null || "".equals(cell.getStringCellValue())) {
                        if (!properties.isNullable()) {
                            errorList.add(new ColumnError(i, j, null, "不能为空"));
                        } else {
                            cellMap.put(fieldName, null);
                        }
                    } else {
                        if (parser == null) {
                            cellMap.put(fieldName, cell.toString());
                        } else {
                            Object o = null;
                            try {
                                o = parser.parse(cell.getStringCellValue().trim(), schoolId);
                            } catch (ParserException e) {
                                errorList.add(new ColumnError(i, j, cell.getStringCellValue(), e.getMessage()));
                            }
                            if (o == null && !properties.isNullable()) {
                                errorList.add(new ColumnError(i, j, null, "无对应数据"));
                            } else {
                                cellMap.put(fieldName, o);
                            }
                        }
                    }
                    j++;
                }
                list.add(cellMap);
            }

            if (errorList.size() > 0) {
                StringBuffer sb = new StringBuffer();
                sb.append(sheet.getSheetName() + ":\n");
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

    public List<Map<String, Object>> redXlsx(String tableName, File file, String r, Long schoolId, Long classesId)
            throws Exception {
        List<ColumnProperties> propertiesList = null;

        propertiesList = (List<ColumnProperties>) excelParserColumn.getClass()
                .getMethod("get" + tableName + "ParserList", new Class[]{Long.class, Long.class})
                .invoke(excelParserColumn, new Object[]{schoolId, classesId});

        if (propertiesList == null) {
            throw new RuntimeException("参数错误");
        }

        List<ColumnError> errorList = new ArrayList<ColumnError>();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            XSSFWorkbook book = new XSSFWorkbook(is);
            XSSFSheet sheet = book.getSheetAt(0);
            // System.out.println(sheet.getSheetName());
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
                    if (row.getCell(j) == null || cell.getStringCellValue() == null
                            || "".equals(cell.getStringCellValue())) {
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
