package com.yaga.kanboardmobile.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yaga.kanboardmobile.R;
import com.yaga.kanboardmobile.model.Task;
import com.yaga.kanboardmobile.repository.TaskRepository;

public class AddTaskActivity extends AppCompatActivity {

    private EditText taskInput;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskInput = findViewById(R.id.editTextTask);
        addButton = findViewById(R.id.buttonAdd);

        int boardId = getIntent().getIntExtra("boardId", -1);
        if (boardId == -1) {
            Toast.makeText(this, "Ошибка: доска не найдена", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = taskInput.getText().toString().trim();
                if (!task.isEmpty()) {
                    TaskRepository.addTask(new Task(task, boardId));

                    Toast.makeText(AddTaskActivity.this, "Задача добавлена: " + task, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddTaskActivity.this, "Введите текст задачи", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
