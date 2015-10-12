package cn.com.weixunyun.child;

import cn.com.weixunyun.child.util.ThrowableUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.apache.http.HttpStatus;
import org.apache.wink.server.handlers.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WinkHandlersFactory extends HandlersFactory {

    @Override
    public List<? extends RequestHandler> getRequestHandlers() {
        List<RequestHandler> handlerList = new ArrayList<RequestHandler>();
        handlerList.addAll(super.getRequestHandlers());
        handlerList.add(new RequestHandler() {


            @Override
            public void init(Properties properties) {
            }

            @Override
            public void handleRequest(MessageContext context, HandlersChain chain) throws Throwable {

                try {
                    chain.doChain(context);
                    context.setAttribute(Integer.class, HttpStatus.SC_OK);

                } catch (WebApplicationException e) {

                    context.setAttribute(Integer.class, e.getResponse().getStatus());
                    context.setResponseEntity(ThrowableUtils.getRootCause(e).getMessage());

                } catch (Exception e) {

                    context.setAttribute(Integer.class, HttpStatus.SC_INTERNAL_SERVER_ERROR);
                    context.setResponseEntity(ThrowableUtils.getRootCause(e).getMessage());

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

                System.out.println("********** ResponseHandlers *********");
                context.setResponseMediaType(MediaType.APPLICATION_JSON_TYPE);

                if (isWeb(context)) {
                    chain.doChain(context);
                    return;
                }

                Object obj = context.getResponseEntity();

                if (obj == null || obj.getClass() != ResultEntity.class) {

                    if (!context.getHttpMethod().equals("GET")) {
                        context.setResponseStatusCode(HttpStatus.SC_OK);
                    }

                    int code = context.getResponseStatusCode();
                    ResultEntity resultEntity = new ResultEntity(code,
                            Response.Status.fromStatusCode(code).getReasonPhrase(),
                            context.getResponseEntity());

                    int resultCode = context.getAttribute(Integer.class);
                    if (HttpStatus.SC_OK != resultCode) {
                        resultEntity = new ResultEntity(resultCode, context.getResponseEntity().toString(), null);
                    }

                    SerializeConfig config = new SerializeConfig();
                    config.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));

                    String str = JSON.toJSONString(resultEntity, config,
                            SerializerFeature.WriteMapNullValue, SerializerFeature.SortField);

                    context.setResponseEntity(str);
                }

                System.out.println("========>>>>resultEntity:" + context.getResponseEntity());

                chain.doChain(context);
            }

            @Override
            public void init(Properties props) {

            }
        });
        return handlerList;
    }

    private boolean isWeb(MessageContext context) {
        boolean isWeb = false;
        String WEB = "eb45324a84d32182e74ac80c71d6f1dc";

        Cookie[] cookies = context.getAttribute(HttpServletRequest.class).getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("code".equals(cookie.getName()) && WEB.equals(cookie.getValue())) {
                    isWeb = true;
                    break;
                }
            }
        }
        return isWeb;
    }


  /*  @Override
    public List<? extends ResponseHandler> getErrorHandlers() {
        List<ResponseHandler> handlerList = new ArrayList<ResponseHandler>();

        handlerList.add(new ResponseHandler() {
            @Override
            public void handleResponse(MessageContext context, HandlersChain chain) throws Throwable {
                System.out.println("ERROR===");
                int code = context.getResponseStatusCode();

                ResultEntity resultEntity = new ResultEntity(code,
                        Response.Status.fromStatusCode(code) != null
                                ? Response.Status.fromStatusCode(code).getReasonPhrase() :
                                "请求错误",
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
        return new ArrayList<ResponseHandler>();
    }*/
}
