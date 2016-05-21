package com.andreoid.EuAluno.models;


public class ServerResponse {

    private String result;
    private String message;
    private boolean aux;
    private User user;
    private ListaDeCursos cursos;
    private ListaDeDisciplinas listaDeDisciplinas;
    private String name;

    public String getResult() {
        return result;
    }

    public boolean isAux() {
        return aux;
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

    public ListaDeDisciplinas getListaDeDisciplinas() {
        return listaDeDisciplinas;
    }

    public String getName() {
        return name;
    }
}
