/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;
import java.util.Date;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.SbRecibos;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.sel.citas.data.CitaTurComprobante;
/**
 *
 * @author rarevalo
 */
public class CitaDetalleArma {
    public String id;
    private Long idKey;
    private Long nroLicencia;
    private Date fecVencimiento;
    private String tipoLicencia;
    private String nroSerie;    
    private String nroRua;
    private String tipoArma;
    private String marca;
    private String modelo;
    private String calibre;
    private String estado;
    private String situacion;        
    private CitaTipoGamac situacionId;
    private CitaTipoGamac estadoId;
    private CitaTipoBase tipoEvaluado;    
    private AmaArma armaId;
    private Date fecPrc;
    private Long turLicenciaRegId;
    private int paraLista;
    private AmaModelos modeloId;
    private String nroLicenciaCaf;
    private SbRecibos reciboArma;
    private String modalidadArma;
    private String tipoInternamiento;
    private CitaTipoGamac tipoInternamientoId;
    private AmaCatalogo tipoArmaId;
    private CitaTurComprobante comprobantePago;
    private Long actaInternamientoId;
    private AmaCatalogo MarcaId;

    public Long getIdKey() {
        return idKey;
    }

    public void setIdKey(Long idKey) {
        this.idKey = idKey;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getNroLicencia() {
        return nroLicencia;
    }

    public void setNroLicencia(Long nroLicencia) {
        this.nroLicencia = nroLicencia;
    }

    public Date getFecVencimiento() {
        return fecVencimiento;
    }

    public void setFecVencimiento(Date fecVencimiento) {
        this.fecVencimiento = fecVencimiento;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    public String getNroRua() {
        return nroRua;
    }

    public void setNroRua(String nroRua) {
        this.nroRua = nroRua;
    }

    public String getTipoArma() {
        return tipoArma;
    }

    public void setTipoArma(String tipoArma) {
        this.tipoArma = tipoArma;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCalibre() {
        return calibre;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public CitaTipoGamac getSituacionId() {
        return situacionId;
    }

    public void setSituacionId(CitaTipoGamac situacionId) {
        this.situacionId = situacionId;
    }

    public CitaTipoGamac getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(CitaTipoGamac estadoId) {
        this.estadoId = estadoId;
    }

    public CitaTipoBase getTipoEvaluado() {
        return tipoEvaluado;
    }

    public void setTipoEvaluado(CitaTipoBase tipoEvaluado) {
        this.tipoEvaluado = tipoEvaluado;
    }

    public Date getFecPrc() {
        return fecPrc;
    }

    public void setFecPrc(Date fecPrc) {
        this.fecPrc = fecPrc;
    }

    public AmaArma getArmaId() {
        return armaId;
    }

    public void setArmaId(AmaArma armaId) {
        this.armaId = armaId;
    }

    public Long getTurLicenciaRegId() {
        return turLicenciaRegId;
    }

    public void setTurLicenciaRegId(Long turLicenciaRegId) {
        this.turLicenciaRegId = turLicenciaRegId;
    }

    public int getParaLista() {
        return paraLista;
    }

    public void setParaLista(int paraLista) {
        this.paraLista = paraLista;
    }

    public AmaModelos getModeloId() {
        return modeloId;
    }

    public void setModeloId(AmaModelos modeloId) {
        this.modeloId = modeloId;
    }

    public String getNroLicenciaCaf() {
        return nroLicenciaCaf;
    }

    public void setNroLicenciaCaf(String nroLicenciaCaf) {
        this.nroLicenciaCaf = nroLicenciaCaf;
    }


    /*@Override
    protected void finalize() throws Throwable {
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
    }*/

    public SbRecibos getReciboArma() {
        return reciboArma;
    }

    public void setReciboArma(SbRecibos reciboArma) {
        this.reciboArma = reciboArma;
    }

    public String getModalidadArma() {
        return modalidadArma;
    }

    public void setModalidadArma(String modalidadArma) {
        this.modalidadArma = modalidadArma;
    }

    public String getTipoInternamiento() {
        return tipoInternamiento;
    }

    public void setTipoInternamiento(String tipoInternamiento) {
        this.tipoInternamiento = tipoInternamiento;
    }
    
    public CitaTipoGamac getTipoInternamientoId() {
        return tipoInternamientoId;
    }

    public void setTipoInternamientoId(CitaTipoGamac tipoInternamientoId) {
        this.tipoInternamientoId = tipoInternamientoId;
    }

    public AmaCatalogo getTipoArmaId() {
        return tipoArmaId;
    }

    public void setTipoArmaId(AmaCatalogo tipoArmaId) {
        this.tipoArmaId = tipoArmaId;
    }
    
    public CitaTurComprobante getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(CitaTurComprobante comprobantePago) {
        this.comprobantePago = comprobantePago;
    }
    
    public Long getActaInternamientoId() {
        return actaInternamientoId;
    }

    public void setActaInternamientoId(Long actaInternamientoId) {
        this.actaInternamientoId = actaInternamientoId;
    }

    public AmaCatalogo getMarcaId() {
        return MarcaId;
    }

    public void setMarcaId(AmaCatalogo MarcaId) {
        this.MarcaId = MarcaId;
    }    
    
}
