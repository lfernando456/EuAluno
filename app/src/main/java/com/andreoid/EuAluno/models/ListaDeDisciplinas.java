package com.andreoid.EuAluno.models;


import java.util.ArrayList;
import java.util.List;

public class ListaDeDisciplinas {

    private List<Disciplina> disciplinas = new ArrayList<>();

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }



    public class Disciplina {
        private String idDisciplina;
        private String idCurso;
        private String nome;
        private String nomeTurma;
        private String ano;

        public String getNomeTurma() {
            return nomeTurma;
        }

        public void setNomeTurma(String nomeTurma) {
            this.nomeTurma = nomeTurma;
        }

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
