package cn.com.weixunyun.child;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.wink.server.handlers.HandlersChain;
import org.apache.wink.server.handlers.HandlersFactory;
import org.apache.wink.server.handlers.MessageContext;
import org.apache.wink.server.handlers.RequestHandler;
import org.apache.wink.server.handlers.ResponseHandler;
import org.apache.wink.server.internal.handlers.SearchResult;

import cn.com.weixunyun.child.model.pojo.Popedom;
import cn.com.weixunyun.child.model.service.PopedomService;
import cn.com.weixunyun.child.model.service.ServiceFactory;

public class WinkHandlersFactory extends HandlersFactory {

	@Override
	public List<? extends RequestHandler> getRequestHandlers() {
		List<RequestHandler> handlerList = new ArrayList<RequestHandler>();
		handlerList.addAll(super.getRequestHandlers());
		handlerList.add(new RequestHandler() {

			private Map<String, List<String>> popedomMap;
			private static final int ACTIONS = 16;

			@Override
			public void init(Properties properties) {
				popedomMap = new HashMap<String, List<String>>();

				try {
					Method[] methods = new Method[ACTIONS];
					for (int i = 0; i < ACTIONS; i++) {
						methods[i] = Popedom.class.getMethod("getAction" + i);
					}

					PopedomService popedomService = ServiceFactory.getService(PopedomService.class);
					for (Popedom popedom : popedomService.select()) {
						List<String> actionList = new ArrayList<String>();

						for (int i = 0; i < 16; i++) {
							Object o = methods[i].invoke(popedom);
							if (o != null) {
								actionList.add(o.toString());
							}
						}
						popedomMap.put(popedom.getCode(), actionList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void handleRequest(MessageContext context, HandlersChain chain) throws Throwable {
				
				if (true) {
					chain.doChain(context);
					return;
				}
				System.out.println("+++++++++++++++++++++++");
				SearchResult sr = context.getAttribute(SearchResult.class);

				String resourcePath = sr.getResource().getRecord().getMetadata().getPath();
				if ("/auth".equals(resourcePath)) {
					chain.doChain(context);
				} else {
					String rsessionid = context.getHttpHeaders().getCookies().get("rsessionid").getValue();
					Map<String, Integer> authedPopedomMap = Session.getInstance(rsessionid).get("popedom");

					String action = context.getHttpMethod();

					String methodPath = sr.getMethod().getMetadata().getPath();
					if (methodPath != null) {
						action += " " + methodPath;
					}

					List<String> actionList = popedomMap.get(resourcePath);
					System.out.println(actionList);
					System.out.println(context.getHttpMethod() + " " + action);
					int actionIndex = -1;
					if (actionList != null) {
						actionIndex = actionList.indexOf(action);
					}
					System.out.println(actionIndex);
					if (actionIndex == -1) {
						context.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
					} else {
						Integer authedIndex = 0;
						if (authedPopedomMap.containsKey(resourcePath)) {
							authedIndex = authedPopedomMap.get(resourcePath);
						}
						actionIndex = 1 << actionIndex;
						System.out.println(authedIndex + " - " + actionIndex);
						if ((authedIndex & actionIndex) == 0) {
							context.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
						} else {
							chain.doChain(context);
						}
					}
				}
			}

		});
		return handlerList;
	}
}
