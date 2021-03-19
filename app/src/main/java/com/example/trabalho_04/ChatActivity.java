package com.example.trabalho_04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.trabalho_04.entidade.User;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

public class ChatActivity extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Receber objeto de outra activity
        User user = getIntent().getExtras().getParcelable("user");
        getSupportActionBar().setTitle(user.getNome()); //Setar nome no action bar

        RecyclerView recyclerView = findViewById(R.id.recyclerChat);
        adapter = new GroupAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(false));
        adapter.add(new MessageItem(false));
        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(false));
    }

    //Gerenciar itens do recycler view
    private class MessageItem extends Item<ViewHolder> {

        //Conferir se o usuário está enviando
        private final boolean enviando;

        private MessageItem(boolean enviando) {
            this.enviando = enviando;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

        }

        @Override
        public int getLayout() {
            if(enviando){ return R.layout.example_chat_envio; }
            else return R.layout.example_chat_recebimento;
        }
    }

}