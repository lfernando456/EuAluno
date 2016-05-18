package com.andreoid.EuAluno.models;

public class CardItemTopicoModel {
    public String idTopico;
    public String title;
    public String content;
    public String professor;
    public String disciplina;
    public String views;
    public String replies_number;

    public CardItemTopicoModel(String idTopico, String title, String content, String professor, String disciplina, String views, String replies_number) {
        this.idTopico = idTopico;
        this.title = title;
        this.content = content;
        this.professor = professor;
        this.disciplina = disciplina;
        this.views = views;
        this.replies_number = replies_number;
    }
}
