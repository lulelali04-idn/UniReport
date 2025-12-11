package com.androidtask.unireport;

public class Report {
    // Fields matching our database columns
    private int id;
    private String title;
    private String description;
    private String location;

    // Constructor
    public Report(int id, String title, String description, String location) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
    }

    // toString is what the ListView uses to decide what text to display
    @Override
    public String toString() {
        return "📍 " + location + ": " + title;
    }

    // Getters (Good practice for expansion later)
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
}
