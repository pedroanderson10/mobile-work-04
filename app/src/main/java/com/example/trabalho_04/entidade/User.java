package com.example.trabalho_04.entidade;

public class User {

    private String id, nome;


    public User(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public User(){

    }


    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

}
