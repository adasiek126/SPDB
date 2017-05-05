package pl.edu.pw.elka.spdb.providers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.cxf.helpers.IOUtils;
import pl.edu.pw.elka.spdb.adapters.gson.RouteGsonAdapter;
import pl.edu.pw.elka.spdb.adapters.list.RouteListAdapter;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Consumes("application/json")
@Produces("application/json")
@Provider
public class RouteListProvider implements MessageBodyWriter<RouteListAdapter>, MessageBodyReader<RouteListAdapter> {
    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return aClass.equals(RouteListAdapter.class);
    }

    @Override
    public long getSize(RouteListAdapter route, Class<?> aClass, Type type, Annotation[] annotations,
                        MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(RouteListAdapter route, Class<?> aClass, Type type, Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String, Object> stringObjectMultivaluedMap, OutputStream outputStream) throws
            IOException, WebApplicationException {
        List<RouteGsonAdapter> adapterList = route.getRoutes().stream().map(RouteGsonAdapter::new).collect(Collectors
                .toList());
        String routesAsJson = new Gson().toJson(adapterList, new TypeToken<List<RouteGsonAdapter>>() {
        }.getType());

        outputStream.write(routesAsJson.getBytes());
    }

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return aClass.equals(RouteListAdapter.class);
    }

    @Override
    public RouteListAdapter readFrom(Class<RouteListAdapter> routeListAdapterClass, Type type,
                                     Annotation[] annotations, MediaType mediaType,
                                     MultivaluedMap<String, String> stringStringMultivaluedMap,
                                     InputStream inputStream) throws IOException, WebApplicationException {
        String json = IOUtils.toString(inputStream);
        List<RouteGsonAdapter> routesFromJson = new Gson().fromJson(json, new TypeToken<List<RouteGsonAdapter>>() {
        }.getType());

        return new RouteListAdapter(routesFromJson.stream().map(r -> r.toRoute()).collect(Collectors.toList()));
    }
}
