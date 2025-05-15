package com.yaga.kanboardmobile.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yaga.kanboardmobile.R;
import com.yaga.kanboardmobile.model.Task;
import com.yaga.kanboardmobile.repository.TaskRepository;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private RecyclerView recyclerTasks;
    private TextView textBoardTitle;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        recyclerTasks = findViewById(R.id.recyclerTasks);
        textBoardTitle = findViewById(R.id.textBoardTitle);

        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));

        int boardId = getIntent().getIntExtra("boardId", -1);
        String boardTitle = getIntent().getStringExtra("boardTitle");

        textBoardTitle.setText("Задачи: " + boardTitle);

        taskList = TaskRepository.getTasksForBoard(boardId);


        taskAdapter = new TaskAdapter(taskList);
        recyclerTasks.setAdapter(taskAdapter);
    }
}
