package com.yaga.kanboardmobile.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yaga.kanboardmobile.R;
import com.yaga.kanboardmobile.model.Task;
import com.yaga.kanboardmobile.repository.TaskRepository;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTaskText;
    private Button buttonSave;

    private int boardId;
    private int taskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTaskText = findViewById(R.id.editTaskInput);
        buttonSave = findViewById(R.id.buttonSaveEdit);

        // Получаем данные из Intent
        String taskText = getIntent().getStringExtra("taskText");
        boardId = getIntent().getIntExtra("boardId", -1);
        taskIndex = getIntent().getIntExtra("taskIndex", -1);

        editTaskText.setText(taskText);

        buttonSave.setOnClickListener(v -> {
            String updatedText = editTaskText.getText().toString().trim();
            if (!updatedText.isEmpty()) {
                TaskRepository.updateTask(boardId, taskIndex, updatedText);
                Toast.makeText(this, "Задача обновлена", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Введите текст задачи", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
