package com.caeumc.caeumc;



/**
 * Created by Lucas Varjao on 25/08/2015.
 */
public class EventosListModel {

    private String Descricao;
    private Integer Data;
    private Integer HoraInicio;
    private Integer HoraFinal;
    private String Local;

    public EventosListModel() {

    }

    public EventosListModel (String descricao, Integer data, Integer horaInicio, Integer horaFinal, String local) {
        this.Descricao = descricao;
        this.Data = data;
        this.HoraInicio = horaInicio;
        this.HoraFinal = horaFinal;
        this.Local = local;
    }

    public void setDescricao(String descricao) {this.Descricao = descricao;}
    public void setData (Integer data) {this.Data = data;}
    public void setHoraInicio (Integer horaInicio) {this.HoraInicio = horaInicio;}
    public void setHoraFinal (Integer horaFinal) {this.HoraFinal = horaFinal;}
    public void setLocal (String local) {this.Local = Local;}

    public String getDescricao() {return this.Descricao;}
    public Integer getData() {return  this.Data;}
    public Integer getHoraInicio() {return  this.HoraInicio;}
    public Integer getHoraFinal() {return  this.HoraFinal;}
    public String getLocal() {return this.Local;}
}
