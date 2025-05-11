package com.example.turfly.models;

public class TurfModel {
    private String id;
    private String name;
    private String location;
    private double rating;
    private String mapsLink;
    private String imageUrl;
    private String area;

    public TurfModel() {} // Required for Firebase

    // ✅ Correct Constructor with 7 Parameters
    public TurfModel(String id, String name, String location, double rating, String mapsLink, String imageUrl, String area) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.mapsLink = mapsLink;
        this.imageUrl = imageUrl;
        this.area = area;
    }

    // ✅ Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getRating() { return rating; }
    public String getMapsLink() { return mapsLink; }
    public String getImageUrl() { return imageUrl; }
    public String getArea() { return area; }
}
