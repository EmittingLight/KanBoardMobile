package com.yaga.kanboardmobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull; // ← ВАЖНО: вот это добавь!
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yaga.kanboardmobile.R;
import com.yaga.kanboardmobile.model.Task;
import com.yaga.kanboardmobile.repository.TaskRepository;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private RecyclerView recyclerTasks;
    private TextView textBoardTitle;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private FloatingActionButton fabAddTask;
    private int boardId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        recyclerTasks = findViewById(R.id.recyclerTasks);
        textBoardTitle = findViewById(R.id.textBoardTitle);
        fabAddTask = findViewById(R.id.fabAddTask);

        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));

        boardId = getIntent().getIntExtra("boardId", -1);

        String boardTitle = getIntent().getStringExtra("boardTitle");

        textBoardTitle.setText("Задачи: " + boardTitle);

        taskList = TaskRepository.getTasksForBoard(boardId);
        taskAdapter = new TaskAdapter(taskList);
        recyclerTasks.setAdapter(taskAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                taskList.remove(position);
                taskAdapter.notifyItemRemoved(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerTasks);


        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class);
                intent.putExtra("boardId", boardId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskList.clear();
        taskList.addAll(TaskRepository.getTasksForBoard(boardId));
        taskAdapter.notifyDataSetChanged();
    }
}
