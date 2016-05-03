package com.andreoid.EuAluno.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luisf on 03/05/2016.
 */
public class ListaDeTurmas {

    private List<Turma> turma = new ArrayList<Turma>();

    public List<Turma> getTurmas() {
        return turma;
    }



    public class Turma{
        private String idTurma;
        private String nome;

        public String getIdTurma() {
            return idTurma;
        }

        public void setIdTurma(String idTurma) {
            this.idTurma = idTurma;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }
}
