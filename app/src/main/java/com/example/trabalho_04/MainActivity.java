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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText textEmail, textSenha;
    private Button btnLogin, btnCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textEmail = findViewById(R.id.emailLogin);
        textSenha = findViewById(R.id.senhaLogin);

    }

    public void realizarLogin(View v){
        String email = textEmail.getText().toString();
        String senha = textSenha.getText().toString();

        Log.i("Teste", "Email : " + email);
        Log.i("Teste", "Senha : " + senha);

        if(email == null || email.isEmpty() || senha == null || senha.isEmpty() ){
            Toast.makeText(this, "Todos os dados devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        autenticarUser(email, senha);

    }


    //Autenticar usuário no firestore
    public void autenticarUser(String email , String senha){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i("testeAutenticação", task.getResult().getUser().getUid());

                        Intent intent = new Intent(MainActivity.this, MensagensActivity.class);
                        //Fazer que activity seja a principal
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("testeAutenticação", e.getMessage());
                    }
                });
    }


    public void cadastrarUser(View v){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

}