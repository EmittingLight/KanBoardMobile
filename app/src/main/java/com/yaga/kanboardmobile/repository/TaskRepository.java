package com.yaga.kanboardmobile.repository;

import com.yaga.kanboardmobile.model.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {
    private static final Map<Integer, List<Task>> taskMap = new HashMap<>();

    public static void addTask(Task task) {
        List<Task> tasks = taskMap.get(task.getBoardId());
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
        taskMap.put(task.getBoardId(), tasks);
    }

    public static List<Task> getTasksForBoard(int boardId) {
        List<Task> tasks = taskMap.get(boardId);
        if (tasks == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(tasks);
    }

    public static void deleteTask(Task task) {
        List<Task> tasks = taskMap.get(task.getBoardId());
        if (tasks != null) {
            tasks.remove(task);
        }
    }

    public static void updateTask(int boardId, int index, String newText) {
        List<Task> tasks = taskMap.get(boardId);
        if (tasks != null && index >= 0 && index < tasks.size()) {
            tasks.get(index).setText(newText);
        }
    }
}
