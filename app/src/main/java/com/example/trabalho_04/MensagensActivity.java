package com.example.trabalho_04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.trabalho_04.entidade.Contato;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.List;

public class MensagensActivity extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagens);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMensagensContatos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GroupAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {

                //Abrir activity de contatos
                Intent intent = new Intent(MensagensActivity.this, ContatosActivity.class);
                startActivity(intent);
            }
        });

        verificarAutenticacao();
        buscarUltimaMsg();

    }

    private void buscarUltimaMsg() {
        String id = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("/ultimas-mensagens")
                .document(id)
                .collection("contatos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<DocumentChange> documentChanges = value.getDocumentChanges();

                        if(documentChanges != null){
                            // Para cada documento, conferir se o tipo dele é igual ao documento de adição
                            //Se for true, criar um contato desse tipo
                            for (DocumentChange doc : documentChanges){
                                if(doc.getType() == DocumentChange.Type.ADDED){
                                    Contato contato = doc.getDocument().toObject(Contato.class);

                                    adapter.add(new ContatoItem(contato));
                                }
                            }
                        }

                    }
                });

    }

    private void verificarAutenticacao() {
        if(FirebaseAuth.getInstance().getUid() == null){
            Intent intent = new Intent(MensagensActivity.this, MainActivity.class);
            //Fazer que activity seja a principal
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.contatos:
                Intent intent = new Intent(MensagensActivity.this, ContatosActivity.class);
                startActivity(intent);
                break;
            case R.id.chatGrupo:
                Intent intent2 = new Intent(MensagensActivity.this, ChatGrupoActivity.class);
                startActivity(intent2);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                verificarAutenticacao();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private class ContatoItem extends Item<ViewHolder> {

        private Contato contato;

        private ContatoItem(Contato contato) {
            this.contato = contato;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView nomeUsuario = viewHolder.itemView.findViewById(R.id.textViewChat);
            TextView ultimaMsg = viewHolder.itemView.findViewById(R.id.textViewChatUltimaMsg);

            nomeUsuario.setText(contato.getNomeUsuario());
            ultimaMsg.setText(contato.getUltimaMsg());

        }

        @Override
        public int getLayout() {
            return R.layout.example_contato_ultima_msg;
        }
    }

}