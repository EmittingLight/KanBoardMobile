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
import com.yaga.kanboardmobile.data.TaskDao;
import androidx.appcompat.widget.Toolbar;


import android.view.Menu;
import android.view.MenuItem;



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
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        recyclerTasks = findViewById(R.id.recyclerTasks);
        textBoardTitle = findViewById(R.id.textBoardTitle);
        fabAddTask = findViewById(R.id.fabAddTask);

        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));

        boardId = getIntent().getIntExtra("boardId", -1);

        String boardTitle = getIntent().getStringExtra("boardTitle");

        textBoardTitle.setText("Задачи: " + boardTitle);

        TaskDao taskDao = new TaskDao(this);
        taskList = taskDao.getTasksForBoard(boardId);
        taskDao.close();

        taskAdapter = new TaskAdapter(this, taskList, boardId);

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
                int taskId = removedTask.getId();

                // Удаление из списка и базы
                taskList.remove(position);
                taskAdapter.notifyItemRemoved(position);

                TaskDao taskDao = new TaskDao(TaskListActivity.this);
                taskDao.deleteTask(taskId);
                taskDao.close();

                // Предложить «ОТМЕНИТЬ»
                Snackbar.make(recyclerTasks, "Задача удалена", Snackbar.LENGTH_LONG)
                        .setAction("ОТМЕНИТЬ", v -> {
                            TaskDao undoDao = new TaskDao(TaskListActivity.this);
                            undoDao.addTask(removedTask);
                            undoDao.close();

                            taskList.add(position, removedTask);
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
        TaskDao taskDao = new TaskDao(this);
        taskList.clear();
        taskList.addAll(taskDao.getTasksForBoard(boardId));
        taskDao.close();
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Boolean filter = null;

        int id = item.getItemId();
        if (id == R.id.filter_all) {
            filter = null;
        } else if (id == R.id.filter_completed) {
            filter = true;
        } else if (id == R.id.filter_pending) {
            filter = false;
        }

        loadTasksWithFilter(filter);
        return true;
    }


    // Метод загрузки задач с фильтром
    private void loadTasksWithFilter(Boolean showCompleted) {
        TaskDao taskDao = new TaskDao(this);
        taskList.clear();
        taskList.addAll(taskDao.getTasksFiltered(boardId, showCompleted));
        taskDao.close();
        taskAdapter.notifyDataSetChanged();
    }

}
