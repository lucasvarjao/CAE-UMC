package com.caeumc.caeumc;

import com.orm.SugarRecord;

/**
 * Created by EDNEI on 27/01/2016.
 */
public class AgendasListModel extends SugarRecord<AgendasListModel> {

    private String IDAgenda;
    private String Identificacao;
    private String Endereco;
    private String Compartilhado;
    private String IDUsuario;

    public AgendasListModel() {

    }

    public AgendasListModel (String id, String identificacao, String Endereco, String Compartilhado, String IDUsuario) {
        this.IDAgenda = id;
        this.Identificacao = identificacao;
        this.Endereco = Endereco;
        this.Compartilhado = Compartilhado;
        this.IDUsuario = IDUsuario;

    }

    public String getIdAgenda() {
        return IDAgenda;
    }

    /**
     *
     * @param idAgenda
     *     The idAgenda
     */
    public void setIdAgenda(String idAgenda) {
        this.IDAgenda = idAgenda;
    }

    /**
     *
     * @return
     *     The identificacao
     */
    public String getIdentificacao() {
        return Identificacao;
    }

    /**
     *
     * @param identificacao
     *     The identificacao
     */
    public void setIdentificacao(String identificacao) {
        this.Identificacao = identificacao;
    }

    /**
     *
     * @return
     *     The endereco
     */
    public String getEndereco() {
        return Endereco;
    }

    /**
     *
     * @param endereco
     *     The endereco
     */
    public void setEndereco(String endereco) {
        this.Endereco = endereco;
    }

    /**
     *
     * @return
     *     The compartilhado
     */
    public String getCompartilhado() {
        return Compartilhado;
    }

    /**
     *
     * @param compartilhado
     *     The compartilhado
     */
    public void setCompartilhado(String compartilhado) {
        this.Compartilhado = compartilhado;
    }

    /**
     *
     * @return
     *     The idUsuario
     */
    public String getIdUsuario() {
        return IDUsuario;
    }

    /**
     *
     * @param idUsuario
     *     The idUsuario
     */
    public void setIdUsuario(String idUsuario) {
        this.IDUsuario = idUsuario;
    }



}
