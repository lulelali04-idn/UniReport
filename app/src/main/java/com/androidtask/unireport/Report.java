package com.androidtask.unireport;

public class Report {
    private int id;
    private String title, description, location, category, status;
    private String image;
    public Report(int id, String title, String description, String location, String category, String status, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
        this.status = status;
        this.image = image;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public String getImage() { return image; } // NEW Getter

    @Override
    public String toString() {
        return title;
    }
}