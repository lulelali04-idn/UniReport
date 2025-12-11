package com.androidtask.unireport;

public class Report {
    private int id;
    private String title, description, location, category;
    private String status; // NEW: "Active" or "Fixed"

    // Updated Constructor
    public Report(int id, String title, String description, String location, String category, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
        this.status = status;
    }

    // toString now shows if item is FIXED
    @Override
    public String toString() {
        String prefix = status.equals("Fixed") ? "[FIXED] " : "🔴 ";
        return prefix + title + " (" + location + ")";
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
}