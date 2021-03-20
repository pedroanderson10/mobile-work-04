package com.example.trabalho_04.entidade;

public class Contato {

    private String id;
    private String nomeUsuario;
    private String ultimaMsg;
    private long momentoMsg;

    public Contato(String id, String nomeUsuario, String ultimaMsg, long momentoMsg) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.ultimaMsg = ultimaMsg;
        this.momentoMsg = momentoMsg;
    }

    public Contato() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getUltimaMsg() {
        return ultimaMsg;
    }

    public void setUltimaMsg(String ultimaMsg) {
        this.ultimaMsg = ultimaMsg;
    }

    public long getMomentoMsg() {
        return momentoMsg;
    }

    public void setMomentoMsg(long momentoMsg) {
        this.momentoMsg = momentoMsg;
    }
}
