package com.yaga.kanboardmobile.model;

public class Task {
    private int id;
    private String text;
    private int boardId;
    private boolean completed;

    // 🔹 Конструктор без id (для добавления новой задачи)
    public Task(String text, int boardId) {
        this.text = text;
        this.boardId = boardId;
        this.completed = false;
    }

    // 🔹 Конструктор с id и completed (для чтения из БД)
    public Task(int id, String text, int boardId, boolean completed) {
        this.id = id;
        this.text = text;
        this.boardId = boardId;
        this.completed = completed;
    }

    // 🔹 Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
