package com.yaga.kanboardmobile.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yaga.kanboardmobile.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    private final SQLiteDatabase database;

    public TaskDao(Context context) {
        TaskDatabaseHelper dbHelper = new TaskDatabaseHelper(context);
        this.database = dbHelper.getWritableDatabase();
    }

    // ✅ Добавление новой задачи
    public void addTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_TEXT, task.getText());
        values.put(TaskDatabaseHelper.COLUMN_BOARD_ID, task.getBoardId());
        values.put(TaskDatabaseHelper.COLUMN_COMPLETED, 0); // по умолчанию невыполнена

        database.insert(TaskDatabaseHelper.TABLE_TASKS, null, values);
    }

    // ✅ Получение задач по ID доски
    public List<Task> getTasksForBoard(int boardId) {
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = database.query(
                TaskDatabaseHelper.TABLE_TASKS,
                null,
                TaskDatabaseHelper.COLUMN_BOARD_ID + "=?",
                new String[]{String.valueOf(boardId)},
                null,
                null,
                TaskDatabaseHelper.COLUMN_CREATED_AT + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_ID));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_TEXT));
                int completed = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDatabaseHelper.COLUMN_COMPLETED));

                Task task = new Task(id, text, boardId, completed == 1);
                tasks.add(task);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return tasks;
    }

    // ✅ Удаление задачи по id
    public void deleteTask(int taskId) {
        database.delete(
                TaskDatabaseHelper.TABLE_TASKS,
                TaskDatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(taskId)}
        );
    }

    // ✅ Обновление текста задачи
    public void updateTask(int taskId, String newText) {
        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_TEXT, newText);
        database.update(
                TaskDatabaseHelper.TABLE_TASKS,
                values,
                TaskDatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(taskId)}
        );
    }

    public void toggleTaskCompleted(int taskId, boolean completed) {
        ContentValues values = new ContentValues();
        values.put(TaskDatabaseHelper.COLUMN_COMPLETED, completed ? 1 : 0);
        database.update(
                TaskDatabaseHelper.TABLE_TASKS,
                values,
                TaskDatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(taskId)}
        );
    }


    // ✅ Закрытие базы
    public void close() {
        database.close();
    }
}
