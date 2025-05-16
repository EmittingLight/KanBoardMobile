package com.yaga.kanboardmobile.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yaga.kanboardmobile.R;
import com.yaga.kanboardmobile.model.Task;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> tasks;
    private final int boardId;
    private final Context context;


    public TaskAdapter(Context context, List<Task> tasks, int boardId) {
        this.context = context;
        this.tasks = tasks;
        this.boardId = boardId;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.textTask.setText(task.getText());


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTaskActivity.class);
            intent.putExtra("taskText", task.getText());
            intent.putExtra("boardId", boardId);
            intent.putExtra("taskIndex", position);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textTask = itemView.findViewById(R.id.textTask);
        }
    }
}

