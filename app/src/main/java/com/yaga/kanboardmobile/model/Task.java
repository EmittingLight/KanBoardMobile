package com.yaga.kanboardmobile.model;

public class Task {
    private String text;
    private int boardId;

    public Task(String text, int boardId) {
        this.text = text;
        this.boardId = boardId;
    }

    // ✅ Добавляем второй конструктор
    public Task(int boardId, String text) {
        this.text = text;
        this.boardId = boardId;
    }

    // Геттеры и сеттеры
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
}
