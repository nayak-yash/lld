package models;

public class Location {
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double distanceTo(Location dst) {
        return Math.sqrt(Math.pow(this.latitude - dst.latitude, 2) + Math.pow(this.longitude - dst.longitude, 2));
    }

    @Override
    public String toString() {
        return "(" + latitude + ", " + longitude + ")";
    }
}
