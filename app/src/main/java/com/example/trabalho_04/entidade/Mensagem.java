package com.example.trabalho_04.entidade;

public class Mensagem {

    private String texto;
    private long momentoMsg;
    private String idEnvio, idRecebimento;


    public Mensagem(String texto, long momentoMsg, String idEnvio, String idRecebimento) {
        this.texto = texto;
        this.momentoMsg = momentoMsg;
        this.idEnvio = idEnvio;
        this.idRecebimento = idRecebimento;
    }

    public Mensagem(){

    }


    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public long getMomentoMsg() {
        return momentoMsg;
    }

    public void setMomentoMsg(long momentoMsg) {
        this.momentoMsg = momentoMsg;
    }

    public String getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(String idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getIdRecebimento() {
        return idRecebimento;
    }

    public void setIdRecebimento(String idRecebimento) {
        this.idRecebimento = idRecebimento;
    }
}
