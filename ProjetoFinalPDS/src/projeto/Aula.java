/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;

import java.util.ArrayList;

/**
 *
 * @author 20151094010102
 */
public class Aula {
    private int codAula;
    private String data;
    private String assunto;
    private int matProf;
    private int matAluno;
    private int codTurma;
    private int codisc;
    private ArrayList<Integer> matAlunos = new ArrayList<Integer>();
    private ArrayList<String> status = new ArrayList<String>();

    public int getCodAula() {
        return codAula;
    }

    public void setCodAula(int codAula) {
        this.codAula = codAula;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public int getMatProf() {
        return matProf;
    }

    public void setMatProf(int matProf) {
        this.matProf = matProf;
    }

    public int getMatAluno() {
        return matAluno;
    }

    public void setMatAluno(int matAluno) {
        this.matAluno = matAluno;
    }

    public int getCodTurma() {
        return codTurma;
    }

    public void setCodTurma(int codTurma) {
        this.codTurma = codTurma;
    }

    public int getCodisc() {
        return codisc;
    }

    public void setCodisc(int codisc) {
        this.codisc = codisc;
    }
    
    public ArrayList<Integer> getMatAlunos() {
        return matAlunos;
    }

    public void setMatAlunos(ArrayList<Integer> matAlunos) {
        this.matAlunos = matAlunos;
    }

    public ArrayList<String> getStatus() {
        return status;
    }

    public void setStatus(ArrayList<String> status) {
        this.status = status;
    }
    
}
