package pl.edu.pw.elka.spdb.adapters.gson;

import pl.edu.pw.elka.spdb.coordinates.Coordinates;
import pl.edu.pw.elka.spdb.model.MapEntry;

public class MapEntryGsonAdapter {
    private Long id;
    private double latitude;
    private double longitude;

    public MapEntryGsonAdapter(MapEntry mapEntry) {
        this.id = mapEntry.getId();

        if (mapEntry.getCoordinates() != null) {
            this.latitude = mapEntry.getCoordinates().getLatitude();
            this.longitude = mapEntry.getCoordinates().getLongitude();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public MapEntry toMapEntry() {
        return new MapEntry(id, new Coordinates(latitude, longitude));
    }
}
