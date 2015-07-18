package cn.com.weixunyun.child.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ExcelUtils {

	public static void main(String args[]) throws FileNotFoundException, IOException {
		List<List<String>> listList = new ArrayList<List<String>>();
		for (int i = 0; i < 10; i++) {
			listList.add(Arrays.asList("A", "B", "C", "D"));
		}
		List<Column<List<String>>> columnList = new ArrayList<Column<List<String>>>();
		columnList.add(new Column<List<String>>() {

			@Override
			public String getTitle() {
				return "标题";
			}

			@Override
			public String getValue(List<String> t) {
				return t.get(0);
			}

		});
		columnList.add(new Column<List<String>>() {

			@Override
			public String getTitle() {
				return "内容摘要";
			}

			@Override
			public String getValue(List<String> t) {
				return t.get(1);
			}

		});
		columnList.add(new Column<List<String>>() {

			@Override
			public String getTitle() {
				return "时间";
			}

			@Override
			public String getValue(List<String> t) {
				return t.get(2);
			}

		});
		writeXlsx(new FileOutputStream("a.xlsx"), listList, columnList);
	}

	public static interface Column<T> {
		public String getTitle();

		public String getValue(T t);
	}

	public static <T> void writeXlsx(OutputStream os, List<T> list, List<Column<T>> columnList) throws IOException {
		ZipOutputStream zos = new ZipOutputStream(os);

		for (Map.Entry<String, String> entry : entryMap.entrySet()) {
			if (!entry.getKey().equals("xl/worksheets/sheet1.xml") && !entry.getKey().equals("xl/sharedStrings.xml")) {
				zos.putNextEntry(new ZipEntry(entry.getKey()));
				zos.write(entry.getValue().getBytes("utf-8"));
				zos.flush();
				zos.closeEntry();
			}
		}

		Writer writer = new OutputStreamWriter(zos, Charset.forName("utf-8"));

		zos.putNextEntry(new ZipEntry("xl/sharedStrings.xml"));
		SharedStringsWriter sharedStringsWriter = new SharedStringsWriter(writer);
		sharedStringsWriter.writeBegin();

		String[] ss = new String[columnList.size()];
		for (int i = 0; i < columnList.size(); i++) {
			ss[i] = columnList.get(i).getTitle();
		}
		sharedStringsWriter.write(ss);

		int row = 1;
		for (T t : list) {
			for (int j = 0; j < columnList.size(); j++) {
				ss[j] = columnList.get(j).getValue(t);
			}
			sharedStringsWriter.write(ss);
			row++;
		}

		sharedStringsWriter.writeEnd();
		sharedStringsWriter.flush();

		zos.closeEntry();

		zos.putNextEntry(new ZipEntry("xl/worksheets/sheet1.xml"));
		SheetWriter sheetWriter = new SheetWriter(writer);
		sheetWriter.write(row, columnList.size());
		sheetWriter.flush();

		zos.closeEntry();
		zos.close();
	}

	static final int BUFFER = 1024;

	static Map<String, String> entryMap = new HashMap<String, String>();

	static {
		try {
			ZipInputStream zis = new ZipInputStream(ExcelUtils.class.getResourceAsStream("/templat_shared.xlsx"));
			ZipEntry entry = null;
			while ((entry = zis.getNextEntry()) != null) {
				if (!entry.isDirectory()) {
					StringBuilder sb = new StringBuilder();
					int count = -1;
					byte data[] = new byte[BUFFER];
					while ((count = zis.read(data, 0, BUFFER)) != -1) {
						sb.append(new String(data, 0, count, Charset.forName("utf-8")));
					}
					entryMap.put(entry.getName(), sb.toString());
				}
			}
			zis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class SharedStringsWriter {
		private Writer writer;

		private SharedStringsWriter(Writer writer) {
			this.writer = writer;
		}

		public void writeBegin() throws IOException {
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			writer.write("<sst count=\"\" xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">\n");
		}

		public void write(String[] ss) throws IOException {
			for (String s : ss) {
				writer.write("<si><t>" + s + "</t></si>");
			}
			writer.flush();
		}

		public void writeEnd() throws IOException {
			writer.write("</sst>");
		}

		public void flush() throws IOException {
			writer.flush();
		}

	}

	private static class SheetWriter {
		private Writer writer;

		private SheetWriter(Writer writer) {
			this.writer = writer;
		}

		public void write(int row, int col) throws IOException {
			writer.write("<?xml version=\"1.0\" encoding=\"" + "UTF-8" + "\"?>"
					+ "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
			writer.write("<sheetData>");
			for (int i = 0; i < row; i++) {
				writer.write("<row r=\"" + (i + 1) + "\">");
				for (int j = 0; j < col; j++) {
					writer.write("<c t=\"s\" ");
					writer.write("r=\"" + (char) (j + 'A') + "" + (i + 1) + "\"");
					writer.write(">");
					writer.write("<v>" + (i * col + j) + "</v>");
					writer.write("</c>");
				}
				writer.write("</row>");
				writer.flush();
			}
			writer.write("</sheetData>");
			writer.write("</worksheet>");
		}

		public void flush() throws IOException {
			writer.flush();
		}

	}
}
