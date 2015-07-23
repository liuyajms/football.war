package cn.com.weixunyun.child;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.http.HttpStatus;
import org.apache.wink.server.handlers.*;
import org.apache.wink.server.internal.handlers.SearchResult;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

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

				/*try {
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
*/
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
                    /*String rsessionid = context.getHttpHeaders().getCookies().get("rsessionid").getValue();
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
                    }*/
                }
            }

        });
        return handlerList;
    }

    @Override
    public List<? extends ResponseHandler> getResponseHandlers() {
        List<ResponseHandler> handlerList = new ArrayList<ResponseHandler>();

        handlerList.add(new ResponseHandler() {
            @Override
            public void handleResponse(MessageContext context, HandlersChain chain) throws Throwable {

                context.setResponseMediaType(MediaType.APPLICATION_JSON_TYPE);

                Object obj = context.getResponseEntity();

                if (obj == null || obj.getClass() != ResultEntity.class) {

                    if(!context.getHttpMethod().equals("GET")){
                        context.setResponseStatusCode(HttpStatus.SC_OK);
                    }
//                    if(context.getHttpMethod())
//                    String msg = "";
//                    if(obj == null){
//                        context.setResponseStatusCode(HttpStatus.SC_OK);
//                        msg = "无数据";
//                    }

                    int code = context.getResponseStatusCode();
                    ResultEntity resultEntity = new ResultEntity(code,
                            Response.Status.fromStatusCode(code).getReasonPhrase(),
                            context.getResponseEntity());

                    String str = JSON.toJSONString(resultEntity, SerializerFeature.WriteMapNullValue);

                    context.setResponseEntity(str);
                }

                System.out.println("========>>>>resultEntity:"+context.getResponseEntity());

                chain.doChain(context);
            }

            @Override
            public void init(Properties props) {

            }
        });
        return handlerList;
    }


    /**
     * 未找到请求资源时的错误处理
     * @return
     */
    @Override
    public List<? extends ResponseHandler> getErrorHandlers() {
        List<ResponseHandler> handlerList = new ArrayList<ResponseHandler>();

        handlerList.add(new ResponseHandler() {
            @Override
            public void handleResponse(MessageContext context, HandlersChain chain) throws Throwable {

                int code = context.getResponseStatusCode();

                ResultEntity resultEntity = new ResultEntity(code,
                        Response.Status.fromStatusCode(code).getReasonPhrase(),
                        context.getResponseEntity());

                String str = JSON.toJSONString(resultEntity);
                context.setResponseEntity(str);
                context.setResponseMediaType(MediaType.APPLICATION_JSON_TYPE);

                chain.doChain(context);
            }

            @Override
            public void init(Properties props) {

            }
        });
        return handlerList;
    }
}
