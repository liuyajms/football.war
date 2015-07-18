package cn.com.weixunyun.child;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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

public class AuthFilter extends AbstractResource implements Filter {

	@Override
	public void destroy() {
		noFilterSet.clear();
		noFilterSet = null;
	}

	private Set<String> noFilterSet = new HashSet<String>();

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;

		if (isNoFilter(request.getPathInfo(), request.getMethod())) {
			chain.doFilter(arg0, arg1);
		} else {
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
			if (rsessionid == null) {
				((HttpServletResponse) arg1).sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			} else {
				Boolean authed = Session.getInstance(rsessionid).get("authed");
				if (authed == null || !authed) {
					((HttpServletResponse) arg1).sendError(HttpServletResponse.SC_UNAUTHORIZED);
				} else {
					chain.doFilter(arg0, arg1);
				}
			}
		}

	}

	private boolean isNoFilter(String path, String method) {
		for (String s : noFilterSet) {
			if (path.startsWith(s)) {
				return true;
			}
		}
		if (path.equals("/security/record") && method.equals("POST")) {
			return true;
		}
		return false;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		noFilterSet.add("/auth");
		noFilterSet.add("/contact");
		noFilterSet.add("/global");
	}

}
