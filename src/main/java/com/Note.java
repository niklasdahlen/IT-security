package com;

public class Note {
    private final int id, userId;
    private final String username, title, content;

    public Note(int id, int userId, String username, String title, String content) {
        this.id = id; this.userId = userId; this.username = username;
        this.title = title; this.content = content;
    }
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
}