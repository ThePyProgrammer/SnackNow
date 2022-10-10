package data;

public class Location {

    // package access (makes sense?)
    String address;
    double longitude;
    double latitude;

    public Location() {
        // nothing here
    }

    public Location(String address) {
        this();
        this.address = address;
    }

    public Location(String address, double longitude, double latitude) {
        this(address);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getAddress() {
        return this.address;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    // no setter methods

}
