package com.andreoid.EuAluno.models;


import java.util.ArrayList;
import java.util.List;

public class ListaDeDisciplinas {

    private List<Disciplina> disciplinas = new ArrayList<>();

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }
    public String[] getNomeDisciplinas() {
        String[] nomes = new String[disciplinas.size()];
        for (int i = 0; i < disciplinas.size(); i++) {
            nomes [i]= disciplinas.get(i).getNome();
        }
        return nomes;
    }
    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public class Disciplina {
        private String idDisciplina;
        private String idCurso;
        private String nome;
        private String ano;


        public String getIdDisciplina() {
            return idDisciplina;
        }

        public void setIdDisciplina(String idDisciplina) {
            this.idDisciplina = idDisciplina;
        }

        public String getIdCurso() {
            return idCurso;
        }

        public void setIdCurso(String idCurso) {
            this.idCurso = idCurso;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getAno() {
            return ano;
        }

        public void setAno(String ano) {
            this.ano = ano;
        }
    }
}
