package pl.edu.pw.elka.spdb.dao.entry;

import pl.edu.pw.elka.spdb.model.MapEntry;

public interface IMapEntryDAO {
    MapEntry insertMapEntry(MapEntry mapEntry);
    MapEntry findMapEntryById(Long id);
    MapEntry findNearestMapEntry(double latitude, double longitude);
    MapEntry findNearestPublicTransportStop(double latitude, double longitude);
}
