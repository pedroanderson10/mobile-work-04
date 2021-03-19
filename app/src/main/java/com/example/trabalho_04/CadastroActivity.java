package com.example.trabalho_04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trabalho_04.entidade.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CadastroActivity extends AppCompatActivity {

    private EditText textNome, textEmail, textSenha;
    private Button btnLogin, btnCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        textNome = findViewById(R.id.nomeCadastro);
        textEmail = findViewById(R.id.emailCadastro);
        textSenha = findViewById(R.id.senhaCadastro);
    }

    //Cadastrar usuário e autenticar conta
    public void finalizarCadastro(View v){
        String nome = textNome.getText().toString() ;
        String email = textEmail.getText().toString();
        String senha = textSenha.getText().toString();

        Log.i("Teste", "Nome : " + nome);
        Log.i("Teste", "Email : " + email);
        Log.i("Teste", "Senha : " + senha);

        if(nome == null || nome.isEmpty() || email == null || email.isEmpty() || senha == null || senha.isEmpty()  ){
            Toast.makeText(this, "Todos os dados devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        //Salvar no firestore
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.i("testeAutenticaçãoCadastro", task.getResult().getUser().getUid());
                            salvarUser(nome);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("testeAutenticaçãoCadastro", e.getMessage());
                    }
                });

    }

    //Salvar usuário no firestore
    public void salvarUser(String nome){
        String id = FirebaseAuth.getInstance().getUid();
        String nomeUsuario = nome;

        User user = new User(id, nomeUsuario);

        FirebaseFirestore.getInstance().collection("users")
                .document(id)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.i("testeCadastro", documentReference.getId());

                        Intent intent = new Intent(CadastroActivity.this, MensagensActivity.class);
                        //Fazer que activity seja a principal
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("testeCadastro", e.getMessage());
                    }
                });
    }

}