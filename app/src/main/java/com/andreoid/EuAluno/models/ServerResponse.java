package com.andreoid.EuAluno.models;


public class ServerResponse {

    private String result;
    private String message;
    private User user;
    private ListaDeCursos cursos;
    private Disciplina disciplina;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public ListaDeCursos getListaDeCursos() {
        return cursos;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }
}
