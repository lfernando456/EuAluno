package com.andreoid.EuAluno.models;


public class ServerRequest {

    private String operation;
    private String curso;
    private String ano;
    private User user;
    private ListaDeDisciplinas listaDeDisciplinas;
    private String topicCat;
    private String unique_id;
    private String disciplina_idDisciplina;


    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
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
