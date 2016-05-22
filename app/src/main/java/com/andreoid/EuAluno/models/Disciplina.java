package com.andreoid.EuAluno.models;


public class Disciplina {
        private String idDisciplina;
        private String idCurso;
        private String nome;
        private String nomeTurma;
        private String nomeProfessor;
        private String qtdTopicos;
        private String ano;

        public Disciplina( String idDisciplina,String idCurso,String nome,String nomeTurma,String nomeProfessor,String qtdTopicos,String ano) {
            this.idDisciplina = idDisciplina;
            this.idCurso = idCurso;
            this.nome = nome;
            this.nomeTurma = nomeTurma;
            this.nomeProfessor = nomeProfessor;
            this.qtdTopicos = qtdTopicos;
            this.ano = ano;
        }

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

        public String getQtdTopicos() {
            return qtdTopicos;
        }

        public void setQtdTopicos(String qtdTopicos) {
            this.qtdTopicos = qtdTopicos;
        }

        public String getNomeProfessor() {
            return nomeProfessor;
        }

        public void setNomeProfessor(String nomeProfessor) {
            this.nomeProfessor = nomeProfessor;
        }
}
