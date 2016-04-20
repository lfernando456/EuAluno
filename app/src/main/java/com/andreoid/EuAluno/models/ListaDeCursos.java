package com.andreoid.EuAluno.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by André on 20/04/2016.
 */
public class ListaDeCursos {

    private List<Curso> cursos = new ArrayList<Curso>();

    public List<Curso> getCursos() {
        return cursos;
    }
    public class Curso {
        private String idCurso;
        private String nome;
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
    }

}
