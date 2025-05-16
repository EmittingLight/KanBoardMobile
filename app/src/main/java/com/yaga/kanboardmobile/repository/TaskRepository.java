package com.yaga.kanboardmobile.repository;

import com.yaga.kanboardmobile.model.Task;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static final List<Task> tasks = new ArrayList<>();

    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static List<Task> getTasksForBoard(int boardId) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getBoardId() == boardId) {
                result.add(task);
            }
        }
        return result;
    }

    public static void deleteTask(Task task) {
        tasks.remove(task);
    }
}

