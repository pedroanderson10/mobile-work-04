package com.example.trabalho_04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaDrm;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.trabalho_04.entidade.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private GroupAdapter adapter;
    private EditText editChat;
    private User user;
    private User userLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editChat = findViewById(R.id.editChat);

        // Receber objeto(usuário que irá receber mensagem) de outra activity
        user = getIntent().getExtras().getParcelable("user");
        getSupportActionBar().setTitle(user.getNome()); //Setar nome do usuário do chat no action bar

        RecyclerView recyclerView = findViewById(R.id.recyclerChat);
        adapter = new GroupAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Buscar coleção de usuários para buscar o registro de conversas do usuário logado
        FirebaseFirestore.getInstance().collection("/users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userLogado = documentSnapshot.toObject(User.class);
                        buscarMensagens();
                    }
                });

        /*adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(false));
        adapter.add(new MessageItem(false));      // Teste anterior
        adapter.add(new MessageItem(true));*/
    }

    private void buscarMensagens(){
        if(userLogado != null){
            String idEnvio = userLogado.getId();
            String idRecebimento = user.getId();

            FirebaseFirestore.getInstance().collection("/conversas")
                    .document(idEnvio)
                    .collection(idRecebimento)
                    .orderBy("momentoMsg", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            //Sempre que pegar uma coleção, tem que criar no adapter uma nova mensagem
                            List<DocumentChange> documentChanges = value.getDocumentChanges();

                            if(documentChanges != null){
                                //Cada documento deve ser transfoirmado em objeto do tipo de mensagem, porém, apenas quando existir uma nova mensagem
                                for(DocumentChange doc : documentChanges){
                                    // Conferir se um objeto acabou de ser inserido
                                    if(doc.getType() == DocumentChange.Type.ADDED){
                                        Mensagem mensagem = doc.getDocument().toObject(Mensagem.class);
                                        adapter.add(new MessageItem(mensagem));
                                    }
                                }
                            }
                        }
                    });

        }
    }


    public void enviarMensagem(View v){
        String texto = editChat.getText().toString();
        editChat.setText(null);

        String idEnvio = FirebaseAuth.getInstance().getUid();
        String idRecebimento = user.getId();
        long momentoMsg = System.currentTimeMillis();

        Mensagem mensagem = new Mensagem(texto, momentoMsg, idEnvio, idRecebimento );


        if(!mensagem.getTexto().isEmpty()){
            //Usuário que envia a mensagem
            FirebaseFirestore.getInstance().collection("/conversas")
                    .document(idEnvio)
                    .collection(idRecebimento)
                    .add(mensagem)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("TesteMensagem", documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TesteMensagem", e.getMessage(), e);
                        }
                    });

            //Usuário que recebe a mensagem
            FirebaseFirestore.getInstance().collection("/conversas")
                    .document(idRecebimento)
                    .collection(idEnvio)
                    .add(mensagem)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("TesteMensagem", documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TesteMensagem", e.getMessage(), e);
                        }
                    });
        }

    }

    //Gerenciar itens do recycler view
    private class MessageItem extends Item<ViewHolder> {

        //Conferir se o usuário está enviando
        private final Mensagem mensagem;

        private MessageItem(Mensagem mensagem) {
            this.mensagem = mensagem;
        }


        /*private final boolean enviando;
        private MessageItem(boolean enviando) {     // Teste anterior
            this.enviando = enviando;
        }*/

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView txtMsg = viewHolder.itemView.findViewById(R.id.textChat);
            txtMsg.setText(mensagem.getTexto());
        }

        @Override
        public int getLayout() {
            if(mensagem.getIdEnvio().equals(FirebaseAuth.getInstance().getUid())) {
                return R.layout.example_chat_envio ;
            } else{
                return R.layout.example_chat_recebimento;
            }

            /*if(enviando){ return R.layout.example_chat_envio; }
            else return R.layout.example_chat_recebimento;*/         // Teste anterior
        }
    }

}