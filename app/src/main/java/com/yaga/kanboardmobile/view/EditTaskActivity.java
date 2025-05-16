package com.yaga.kanboardmobile.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yaga.kanboardmobile.R;
import com.yaga.kanboardmobile.data.TaskDao;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTaskText;
    private Button buttonSave;

    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTaskText = findViewById(R.id.editTaskInput);
        buttonSave = findViewById(R.id.buttonSaveEdit);

        // Получаем данные из Intent
        String taskText = getIntent().getStringExtra("taskText");
        taskId = getIntent().getIntExtra("taskId", -1);

        if (taskId == -1) {
            Toast.makeText(this, "Ошибка: задача не найдена", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editTaskText.setText(taskText);

        buttonSave.setOnClickListener(v -> {
            String updatedText = editTaskText.getText().toString().trim();
            if (!updatedText.isEmpty()) {
                TaskDao taskDao = new TaskDao(this);
                taskDao.updateTask(taskId, updatedText);
                taskDao.close();

                Toast.makeText(this, "Задача обновлена", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Введите текст задачи", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
