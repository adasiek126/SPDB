package pl.edu.pw.elka.spdb.providers;

import com.google.gson.Gson;
import org.apache.cxf.helpers.IOUtils;
import pl.edu.pw.elka.spdb.adapters.gson.MapEntryGsonAdapter;
import pl.edu.pw.elka.spdb.model.MapEntry;

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

@Consumes("application/json")
@Produces("application/json")
@Provider
public class MapEntryProvider implements MessageBodyWriter<MapEntry>, MessageBodyReader<MapEntry> {
    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return MapEntry.class.isAssignableFrom(aClass);
    }

    @Override
    public long getSize(MapEntry mapEntry, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(MapEntry mapEntry, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> stringObjectMultivaluedMap, OutputStream outputStream) throws
            IOException, WebApplicationException {
        outputStream.write(new Gson().toJson(new MapEntryGsonAdapter(mapEntry)).getBytes());
    }

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return MapEntry.class.isAssignableFrom(aClass);
    }

    @Override
    public MapEntry readFrom(Class<MapEntry> mapEntryClass, Type type, Annotation[] annotations, MediaType mediaType,
                             MultivaluedMap<String, String> stringStringMultivaluedMap,
                             InputStream inputStream) throws IOException, WebApplicationException {
        String json = IOUtils.toString(inputStream);
        MapEntryGsonAdapter mapEntryAdapter = new Gson().fromJson(json, MapEntryGsonAdapter.class);

        return mapEntryAdapter.toMapEntry();
    }
}
