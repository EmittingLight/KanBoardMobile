package com.yaga.kanboardmobile.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_BOARD_ID = "board_id";
    public static final String COLUMN_COMPLETED = "completed";
    public static final String COLUMN_CREATED_AT = "created_at";

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_TASKS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TEXT + " TEXT NOT NULL, "
            + COLUMN_BOARD_ID + " INTEGER, "
            + COLUMN_COMPLETED + " INTEGER DEFAULT 0, "
            + COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
            + ");";

    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE INDEX idx_board_id ON " + TABLE_TASKS + "(" + COLUMN_BOARD_ID + ")");

        onCreate(db);
    }
}
