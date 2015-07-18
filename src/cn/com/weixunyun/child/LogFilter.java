package cn.com.weixunyun.child;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.model.pojo.Log;
import cn.com.weixunyun.child.model.pojo.Parents;
import cn.com.weixunyun.child.model.pojo.School;
import cn.com.weixunyun.child.model.pojo.Teacher;
import cn.com.weixunyun.child.model.service.LogService;
import cn.com.weixunyun.child.model.service.ServiceFactory;

public class LogFilter extends AbstractResource implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException,
			ServletException {
		chain.doFilter(arg0, arg1);

		try {
			HttpServletRequest request = (HttpServletRequest) arg0;
			String url = request.getPathInfo();
			String method = request.getMethod();

			Long schoolId = null;
			Long userId = null;

			String rsessionid = null;
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("rsessionid".equals(cookie.getName())) {
						rsessionid = cookie.getValue();
						break;
					}
				}
			}
			if (rsessionid != null) {
				Session session = Session.getInstance(rsessionid);
				if (session != null) {
					School school = session.get("school");
					if (school != null) {
						schoolId = school.getId();
					}

					Teacher teacher = session.get("teacher");
					if (teacher != null) {
						userId = teacher.getId();
					}
					Parents parents = session.get("parents");
					if (parents != null) {
						userId = parents.getId();
					}
				}
			}

			Log log = new Log();
			log.setSchoolId(schoolId);
			log.setUserId(userId);
			log.setUrl(url);
			log.setMethod(method);
			log.setTime(new java.sql.Timestamp(System.currentTimeMillis()));

			LogService logService = ServiceFactory.getService(LogService.class);
			logService.insert(log);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
