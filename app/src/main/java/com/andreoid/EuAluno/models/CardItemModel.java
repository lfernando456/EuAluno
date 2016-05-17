package com.andreoid.EuAluno.models;

public class CardItemModel {
    public String idTopico;
    public String title;
    public String content;
    public String professor;
    public String disciplina;
    public String views;


    public CardItemModel(String idTopico,String title, String content,String professor,String disciplina,String views) {
        this.idTopico = idTopico;
        this.title = title;
        this.content = content;
        this.professor = professor;
        this.disciplina = disciplina;
        this.views = views;
    }
}
