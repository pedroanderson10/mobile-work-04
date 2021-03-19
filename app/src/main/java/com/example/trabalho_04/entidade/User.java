package com.example.trabalho_04.entidade;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String id, nome;


    public User(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public User(){

    }

    //Construtor protegido, para mapear todos os atributos para conseguir pegar de volta o seu estado
    protected User(Parcel in) {
        id = in.readString();
        nome = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };



    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nome);
    }
}
