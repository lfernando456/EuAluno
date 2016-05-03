package com.andreoid.EuAluno.models;


public class User {

    private String name;
    private String sno;
    private String email;


    private String unique_id;
    private String password;
   private String tipo;
    private String old_password;
    private String new_password;

    private String siap;
    private String idCurso;
    private String matricula;
    private String ano;

    public String getSiap() {
        return siap;
    }

    public void setSiap(String siap) {
        this.siap = siap;
    }

    public String getIdCurso() {
        return idCurso;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getAno() {
        return ano;
    }

    public String getName() {
        return name;
    }
    public String getSno() {
        return sno;
    }

    public String getEmail() {
        return email;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSno(String sno) {
        this.sno = sno;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setTipo(String tipo) {
       this.tipo = tipo;
    }
    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
    public void setAno(String ano) {
        this.ano = ano;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setIdCurso(String idCurso) {
        this.idCurso = idCurso;
    }
}
