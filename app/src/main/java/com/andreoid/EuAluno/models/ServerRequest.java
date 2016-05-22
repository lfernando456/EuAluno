package com.andreoid.EuAluno.models;


import java.util.List;

public class ServerRequest {

    private String operation;

    private String idCurso;
    private String idTurma;
    private String turma;
    private User user;
    private List<Disciplina> disciplinas;

    private String topic_subject,topic_date,topic_cat,reply_content;
    private String reply_topic;
    private String unique_id;
    private String disciplina_idDisciplina;
    private String has_anexo;
    private String anexo;



    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }




    public void setReply_topic(String reply_topic) {
        this.reply_topic = reply_topic;
    }


    public void setTopic_subject(String topic_subject) {
        this.topic_subject = topic_subject;
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

    public void setListaDeDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
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

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }
}
