package cn.com.weixunyun.child;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.com.weixunyun.child.control.AbstractResource;

public class TimeFilter extends AbstractResource implements Filter {

	private SimpleDateFormat format;
	private BufferedWriter writer;

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		format = new SimpleDateFormat("yyy-MM-dd HH:mm:dd.ss");
		try {
			String path = arg0.getInitParameter("path");
			if (path == null) {
				throw new Exception();
			} else {
				writer = new BufferedWriter(new FileWriter(path, true));
			}
		} catch (Exception e) {
			writer = new BufferedWriter(new Writer() {

				@Override
				public void close() throws IOException {
				}

				@Override
				public void flush() throws IOException {
				}

				@Override
				public void write(char[] cbuf, int off, int len) throws IOException {
					System.out.print(new String(cbuf, off, len));
				}

			});
		}
	}

	@Override
	public void destroy() {
		try {
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		writer = null;
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException,
			ServletException {
		long t = System.currentTimeMillis();
		chain.doFilter(arg0, arg1);

		try {
			HttpServletRequest request = (HttpServletRequest) arg0;
			writer.write("0");
			writer.write("\t");
			writer.write(format.format(new java.util.Date()));
			writer.write("\t");
			writer.write(request.getRequestURI().substring(7));
			writer.write("\t");
			writer.write(request.getMethod());
			writer.write("\t");
			writer.write(Long.toString(System.currentTimeMillis() - t));
			writer.write("\n");
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
