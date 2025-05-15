package com.yaga.kanboardmobile.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yaga.kanboardmobile.R;
import com.yaga.kanboardmobile.model.Board;

import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private List<Board> boards;

    public BoardAdapter(List<Board> boards) {
        this.boards = boards;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_board, parent, false);
        return new BoardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        Board board = boards.get(position);
        holder.nameTextView.setText(board.getName());
        holder.descriptionTextView.setText(board.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, TaskListActivity.class);
                intent.putExtra("boardId", board.getId());
                intent.putExtra("boardTitle", board.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return boards.size();
    }

    static class BoardViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.boardName);
            descriptionTextView = itemView.findViewById(R.id.boardDescription);
        }
    }
}
