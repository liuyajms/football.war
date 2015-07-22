package cn.com.weixunyun.child;

import cn.com.weixunyun.child.control.ClientResource;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import javax.ws.rs.core.Application;
import java.io.File;
import java.util.*;

public class WinkApplication extends Application {

    /**
     * Get the list of service classes provided by this JAX-RS application
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> serviceClasses = new LinkedHashSet<Class<?>>();

        List<String> list = getClz("cn/com/weixunyun/child/module");
        list.addAll(getClz("cn/com/weixunyun/child/control"));
        for (String s : list) {
            try {
                serviceClasses.add(Class.forName(s));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        //获取客户端列表
//        ClientResource.initClients();

        return serviceClasses;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> s = new HashSet<Object>();

        // Register the Jackson provider for JSON

        // Make (de)serializer use a subset of JAXB and (afterwards) Jackson
        // annotations
        // See http://wiki.fasterxml.com/JacksonJAXBAnnotations for more
        // information
        try {
            ObjectMapper mapper = new ObjectMapper();
            AnnotationIntrospector primary = new JaxbAnnotationIntrospector();
            AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
            AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);
            mapper.getDeserializationConfig().setAnnotationIntrospector(pair);
            mapper.getSerializationConfig().setAnnotationIntrospector(pair);

            // Set up the provider
            JacksonJaxbJsonProvider jaxbProvider = new JacksonJaxbJsonProvider();
            jaxbProvider.setMapper(mapper);

            s.add(jaxbProvider);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getClz(String basePkg) {
        List<String> list = new ArrayList<String>();
        String path = WinkApplication.class.getResource("/" + basePkg).getPath();
        File[] ff = new File(path).listFiles();
        for (File f : ff) {
            if (f.isDirectory()) {
                list.addAll(getClz(basePkg + "/" + f.getName()));
            } else {
                if (f.getName().toLowerCase().endsWith("resource.class")) {
                    list.add(basePkg.replace("/", ".") + "." + f.getName().substring(0, f.getName().length() - 6));
                }
            }
        }

        return list;
    }

}