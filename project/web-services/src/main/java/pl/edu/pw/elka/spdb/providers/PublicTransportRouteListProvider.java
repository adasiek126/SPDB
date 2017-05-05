package pl.edu.pw.elka.spdb.providers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.cxf.helpers.IOUtils;
import pl.edu.pw.elka.spdb.adapters.gson.PublicTransportRouteGsonAdapter;
import pl.edu.pw.elka.spdb.adapters.list.PublicTransportRouteListAdapter;

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
public class PublicTransportRouteListProvider implements MessageBodyWriter<PublicTransportRouteListAdapter>,
        MessageBodyReader<PublicTransportRouteListAdapter> {
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return aClass.equals(PublicTransportRouteListAdapter.class);
    }

    @Override
    public PublicTransportRouteListAdapter readFrom(Class<PublicTransportRouteListAdapter>
                                                            publicTransportRouteListAdapterClass, Type type,
                                                    Annotation[] annotations, MediaType mediaType,
                                                    MultivaluedMap<String, String> stringStringMultivaluedMap,
                                                    InputStream inputStream) throws IOException,
            WebApplicationException {
        String json = IOUtils.toString(inputStream);
        List<PublicTransportRouteGsonAdapter> routesFromJson = new Gson().fromJson(json,
                new TypeToken<List<PublicTransportRouteGsonAdapter>>() {
                }.getType()
        );

        return new PublicTransportRouteListAdapter(routesFromJson.stream().map(r -> r.toRoute()).collect(Collectors
                .toList()));
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return aClass.equals(PublicTransportRouteListAdapter.class);
    }

    @Override
    public long getSize(PublicTransportRouteListAdapter publicTransportRouteListAdapter, Class<?> aClass, Type type,
                        Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(PublicTransportRouteListAdapter route, Class<?> aClass, Type type,
                        Annotation[] annotations, MediaType mediaType, MultivaluedMap<String,
            Object> stringObjectMultivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        List<PublicTransportRouteGsonAdapter> adapterList = route.getRoutes().stream().map
                (PublicTransportRouteGsonAdapter::new).collect(Collectors.toList());
        String routesAsJson = new Gson().toJson(adapterList, new TypeToken<List<PublicTransportRouteGsonAdapter>>() {
        }.getType());

        outputStream.write(routesAsJson.getBytes());
    }
}
