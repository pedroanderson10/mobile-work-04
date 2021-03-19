package com.example.trabalho_04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trabalho_04.entidade.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

public class ContatosActivity extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewContatos);
        adapter = new GroupAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buscarUsuarios();

    }

    private void buscarUsuarios() {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.i("TesteBusca", error.getMessage(), error);
                            return;
                        }

                        //Elementos da lista
                        List<DocumentSnapshot> docs =  value.getDocuments();
                        for (DocumentSnapshot doc : docs){
                            User user = doc.toObject(User.class);
                            Log.i("TesteBusca", user.getNome());

                            adapter.add(new UserItem(user));
                        }

                    }
                });
    }

    //Gerenciar itens do recycler view
    private class UserItem extends Item<ViewHolder> {

        private final User user;

        private UserItem(User user) {
            this.user = user;
        }

        @Override
        //Conectar itens para manipulá-los
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            ImageView imgUser = viewHolder.itemView.findViewById(R.id.imageViewUser);
            TextView txtNomeUser = viewHolder.itemView.findViewById(R.id.textViewChat);
            ImageView imgChat = viewHolder.itemView.findViewById(R.id.imageViewButtonChat);

            txtNomeUser.setText(user.getNome());

        }

        @Override
        //Layout de cada item
        public int getLayout() {
            return R.layout.example_contato;
        }
    }
}