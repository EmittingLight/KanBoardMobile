package com.yaga.kanboardmobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yaga.kanboardmobile.R;
import com.yaga.kanboardmobile.model.Task;
import com.yaga.kanboardmobile.repository.TaskRepository;
import com.google.android.material.snackbar.Snackbar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;


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
                Task removedTask = taskList.get(position);

                // Удаляем задачу
                taskList.remove(position);
                TaskRepository.deleteTask(removedTask);
                taskAdapter.notifyItemRemoved(position);

                // Показываем Snackbar с кнопкой "ОТМЕНИТЬ"
                Snackbar.make(recyclerTasks, "Задача удалена", Snackbar.LENGTH_LONG)
                        .setAction("ОТМЕНИТЬ", v -> {
                            taskList.add(position, removedTask);
                            TaskRepository.addTask(removedTask);
                            taskAdapter.notifyItemInserted(position);
                        })
                        .show();
            }
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                paint.setColor(Color.RED);

                // Рисуем красный фон
                if (dX != 0) {
                    Rect background = new Rect(itemView.getLeft(), itemView.getTop(),
                            itemView.getRight(), itemView.getBottom());
                    c.drawRect(background, paint);

                    // Иконка корзины
                    Drawable icon = ContextCompat.getDrawable(TaskListActivity.this, android.R.drawable.ic_menu_delete);
                    if (icon != null) {
                        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                        int iconTop = itemView.getTop() + iconMargin;
                        int iconBottom = iconTop + icon.getIntrinsicHeight();

                        int iconLeft, iconRight;

                        if (dX > 0) { // свайп вправо
                            iconLeft = itemView.getLeft() + iconMargin;
                            iconRight = iconLeft + icon.getIntrinsicWidth();
                        } else { // свайп влево
                            iconRight = itemView.getRight() - iconMargin;
                            iconLeft = iconRight - icon.getIntrinsicWidth();
                        }

                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);
                    }
                }
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
