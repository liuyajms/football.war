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
import javax.servlet.http.HttpServletResponse;

import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.control.ClientResource;

public class ClientFilter extends AbstractResource implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;

		String code = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("code".equals(cookie.getName())) {
					code = cookie.getValue();
					break;
				}
			}
		}

		if (code == null || !ClientResource.clients.containsKey(code)) {
			((HttpServletResponse) arg1).sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} else {
			chain.doFilter(arg0, arg1);
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
