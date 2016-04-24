package com.andreoid.EuAluno.models;


public class ServerRequest {

    private String operation;
    private String curso;
    private String ano;
    private User user;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
