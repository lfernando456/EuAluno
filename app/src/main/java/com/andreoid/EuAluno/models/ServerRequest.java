package com.andreoid.EuAluno.models;


public class ServerRequest {

    private String operation;
    private String curso;
    private String ano;
    private User user;
    private int topicCat;

    public int getTopicCat() {
        return topicCat;
    }

    public void setTopicCat(int topicCat) {
        this.topicCat = topicCat;
    }

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
