package cn.com.weixunyun.child;

import cn.com.weixunyun.child.control.AbstractResource;
import cn.com.weixunyun.child.model.pojo.Log;
import cn.com.weixunyun.child.model.service.LogService;
import cn.com.weixunyun.child.model.service.LogServiceFactory;
import cn.com.weixunyun.child.model.vo.PlayerVO;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogFilter extends AbstractResource implements Filter {

    private static final int BUFFER_SIZE = 100;

    private List<Log> logList;

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

                    PlayerVO player = session.get("player");
                    if (player != null) {
                        userId = player.getId();
                    }
                }
            }

            Log log = new Log();
            log.setUserId(userId);
            log.setUrl(url);
            log.setMethod(method);
            log.setTime(new java.sql.Timestamp(System.currentTimeMillis()));

//			LogService logService = ServiceFactory.getService(LogService.class);
//			logService.insert(log);

            if (logList.size() < BUFFER_SIZE) {
                logList.add(log);
            } else {
                synchronized (logList) {
                    LogService logService = LogServiceFactory.getService(LogService.class);
                    logService.insertBatch(logList);
                    logList.clear();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        logList = Collections.synchronizedList(new ArrayList<Log>(BUFFER_SIZE));
    }

}
