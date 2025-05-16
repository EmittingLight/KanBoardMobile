package com.yaga.kanboardmobile.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yaga.kanboardmobile.R;
import com.yaga.kanboardmobile.data.TaskDao;
import com.yaga.kanboardmobile.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Context context;
    private final List<Task> taskList;
    private final int boardId;

    public TaskAdapter(Context context, List<Task> taskList, int boardId) {
        this.context = context;
        this.taskList = taskList;
        this.boardId = boardId;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.textTask.setText(task.getText());

        // Отображение зачёркивания
        if (task.isCompleted()) {
            holder.textTask.setPaintFlags(holder.textTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textTask.setPaintFlags(holder.textTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Клик по задаче — переключение статуса
        holder.textTask.setOnClickListener(v -> {
            boolean newStatus = !task.isCompleted();
            task.setCompleted(newStatus);

            TaskDao dao = new TaskDao(context);
            dao.toggleTaskCompleted(task.getId(), newStatus);
            dao.close();

            notifyItemChanged(position);
        });

        // Клик на редактирование
        holder.itemView.setOnLongClickListener(v -> {
            Intent intent = new Intent(context, EditTaskActivity.class);
            intent.putExtra("taskId", task.getId());
            intent.putExtra("taskText", task.getText());
            intent.putExtra("boardId", boardId);
            intent.putExtra("taskIndex", position);
            context.startActivity(intent);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textTask = itemView.findViewById(R.id.textTask);
        }
    }
}
