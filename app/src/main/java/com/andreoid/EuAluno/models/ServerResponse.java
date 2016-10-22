package com.andreoid.EuAluno.models;


import java.util.List;

public class ServerResponse {

    private String result;
    private String message;
    private boolean aux;
    private User user;
    private List<Curso> cursos;
    private List<Disciplina> disciplinas;
    private List<Reply> replies;
    private List<Topico> topicos;
    private List<Turma> turmas;
    private List<User> user_viewed;
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

    public String getName() {
        return name;
    }

    public List<Curso> getListaDeCursos() {
        return cursos;
    }

    public List<Disciplina> getListaDeDisciplinas() {
        return disciplinas;
    }

    public List<Reply> getListaDeReplies() {
        return replies;
    }

    public List<Topico> getListaDeTopicos() {
        return topicos;
    }

    public List<Turma> getListaDeTurmas() {
        return turmas;
    }

    public List<User> getListaDeUser_viewed() {
        return user_viewed;
    }
}
