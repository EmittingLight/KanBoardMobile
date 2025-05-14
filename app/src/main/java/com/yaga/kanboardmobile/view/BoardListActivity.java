package com.yaga.kanboardmobile.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yaga.kanboardmobile.R;
import com.yaga.kanboardmobile.model.Board;

import java.util.ArrayList;
import java.util.List;

public class BoardListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BoardAdapter adapter;
    private List<Board> boardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        recyclerView = findViewById(R.id.boardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        boardList = new ArrayList<>();
        boardList.add(new Board(1, "Рабочая доска", "Для текущих задач"));
        boardList.add(new Board(2, "Личное", "Планы, идеи и заметки"));

        adapter = new BoardAdapter(boardList);
        recyclerView.setAdapter(adapter);
    }
}

