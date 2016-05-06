package com.andreoid.EuAluno.models;


public class ServerRequest {

    private String operation;

    private String idCurso;
    private String turma;
    private User user;
    private ListaDeDisciplinas listaDeDisciplinas;
    private String topic_cat;
    private String unique_id;
    private String disciplina_idDisciplina;

    public String getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(String idCurso) {
        this.idCurso = idCurso;
    }
    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }


    public void setDisciplina_idDisciplina(String disciplina_idDisciplina) {
        this.disciplina_idDisciplina = disciplina_idDisciplina;
    }

    public void setListaDeDisciplinas(ListaDeDisciplinas listaDeDisciplinas) {
        this.listaDeDisciplinas = listaDeDisciplinas;
    }

    public String getTopic_cat() {
        return topic_cat;
    }

    public void setTopic_cat(String topic_cat) {
        this.topic_cat = topic_cat;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }



    public void setTurma(String turma) {
        this.turma = turma;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
