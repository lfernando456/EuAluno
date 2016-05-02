package com.andreoid.EuAluno.models;


import java.util.List;

public class ServerRequest {

    private String operation;
    private String curso;
    private String ano;
    private User user;
    private ListaDeDisciplinas listaDeDisciplinas;
    private String topicCat;
    private String aluno_idAluno;
    private String disciplina_idDisciplina;


    public void setAluno_idAluno(String aluno_idAluno) {
        this.aluno_idAluno = aluno_idAluno;
    }


    public void setDisciplina_idDisciplina(String disciplina_idDisciplina) {
        this.disciplina_idDisciplina = disciplina_idDisciplina;
    }

    public void setListaDeDisciplinas(ListaDeDisciplinas listaDeDisciplinas) {
        this.listaDeDisciplinas = listaDeDisciplinas;
    }

    public String getTopicCat() {
        return topicCat;
    }

    public void setTopicCat(String topicCat) {
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
