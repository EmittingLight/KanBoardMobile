package com.yaga.kanboardmobile.model;

public class Task {
    private int boardId;
    private String text;

    public Task(int boardId, String text) {
        this.boardId = boardId;
        this.text = text;
    }

    public int getBoardId() {
        return boardId;
    }

    public String getText() {
        return text;
    }
}

