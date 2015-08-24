package com.caeumc.caeumc;

import com.orm.SugarRecord;

/**
 * Created by Lucas Varjao on 15/08/2015.
 */
public class MateriaListModel extends SugarRecord<MateriaListModel> {

    private String NomeMateria;
    private Double M1;
    private Double M2;
    private Double PI;
    private Double EX;
    private Double NF;
    private Boolean DP;

    public MateriaListModel() {

    }

    public MateriaListModel(String NomeMateria, Double M1, Double M2, Double PI, Double EX, Double NF, Boolean DP) {
        this.NomeMateria = NomeMateria;
        this.M1 = M1;
        this.M2 = M2;
        this.PI = PI;
        this.EX = EX;
        this.NF = NF;
        this.DP = DP;

    }

    public void setNomeMateria(String nomeMateria) {
        this.NomeMateria = nomeMateria;
    }

    public void setM1 (Double M1) {
        this.M1 = M1;
    }

    public void setM2 (Double M2) {
        this.M2 = M2;
    }

    public void setPI (Double PI) {
        this.PI = PI;
    }

    public void setEX (Double EX) {
        this.EX = EX;
    }

    public void setNF (Double NF) {
        this.NF = NF;
    }

    public void setDP (Boolean DP) {this.DP = DP;}

    public String getNomeMateria() {
        return this.NomeMateria;
    }

    public Double getM1() {
        return this.M1;
    }

    public Double getM2() {
        return this.M2;
    }

    public Double getPI() {
        return this.PI;
    }

    public Double getEX() {
        return this.EX;
    }

    public Double getNF() {
        return this.NF;
    }

    public Boolean getDP() {return this.DP;}


}
