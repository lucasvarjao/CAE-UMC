package com.caeumc.caeumc;


import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by Lucas Varjao on 25/08/2015.
 */
public class EventosListModel extends SugarRecord<EventosListModel> {

    private String Descricao;
    private Integer Data;
    private Integer HoraInicio;
    private Integer HoraFinal;
    private String Local;
    private Boolean Feriado;

    @Ignore
    String Mes;
    @Ignore
    Integer DiaEvento;
    @Ignore
    String DiaSemanaEvento;
    @Ignore
    String Semana;

    public EventosListModel() {

    }

    public EventosListModel (String descricao, Integer data, Integer horaInicio, Integer horaFinal, String local, Boolean feriado) {
        this.Descricao = descricao;
        this.Data = data;
        this.HoraInicio = horaInicio;
        this.HoraFinal = horaFinal;
        this.Local = local;
        this.Feriado = feriado;
    }

    public void setDescricao(String descricao) {this.Descricao = descricao;}
    public void setData (Integer data) {this.Data = data;}
    public void setHoraInicio (Integer horaInicio) {this.HoraInicio = horaInicio;}
    public void setHoraFinal (Integer horaFinal) {this.HoraFinal = horaFinal;}
    public void setLocal (String local) {this.Local = Local;}
    public void setFeriado (Boolean feriado) {this.Feriado = feriado;}

    public void setMes (String mes) {this.Mes = mes;}
    public String getMes() {return  this.Mes;}
    public void setDiaSemanaEvento (String diaSemanaEvento) {this.DiaSemanaEvento = diaSemanaEvento;}
    public String getDiaSemanaEvento() {return  this.DiaSemanaEvento;}
    public void setDiaEvento (Integer diaEvento) {this.DiaEvento = diaEvento;}
    public Integer getDiaEvento() {return  this.DiaEvento;}
    public void setSemana (String semana) {this.Semana = semana;}
    public String getSemana() {return  this.Semana;}


    public String getDescricao() {return this.Descricao;}
    public Integer getData() {return  this.Data;}
    public Integer getHoraInicio() {return  this.HoraInicio;}
    public Integer getHoraFinal() {return  this.HoraFinal;}
    public String getLocal() {return this.Local;}
    public Boolean getFeriado() {return this.Feriado;}
}
