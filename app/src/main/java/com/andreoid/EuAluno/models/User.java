package com.andreoid.EuAluno.models;


public class User {

    private String nome;
    private String email;
    private String unique_id;
    private String password;
    private int tipoUsuario;
    private String old_password;
    private String new_password;


    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getUnique_id() {
        return unique_id;
    }
    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setName(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

}
