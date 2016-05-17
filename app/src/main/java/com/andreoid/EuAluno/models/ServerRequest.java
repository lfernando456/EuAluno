package com.andreoid.EuAluno.models;


public class ServerRequest {

    private String operation;

    private String idCurso;
    private String idTurma;
    private String turma;
    private User user;
    private ListaDeDisciplinas listaDeDisciplinas;
    private String topic_subject,topic_date,topic_cat,reply_content;

    private String unique_id;
    private String disciplina_idDisciplina;

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }




    public String getTopic_date() {
        return topic_date;
    }
    public void setTopic_date(String topic_date) {
        this.topic_date = topic_date;
    }
    public String getTopic_subject() {
        return topic_subject;
    }
    public void setTopic_subject(String topic_subject) {
        this.topic_subject = topic_subject;
    }
    public String getIdCurso() {
        return idCurso;
    }
    public void setIdCurso(String idCurso) {
        this.idCurso = idCurso;
    }
    public void setIdTurma(String idTurma) {
        this.idTurma = idTurma;
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
