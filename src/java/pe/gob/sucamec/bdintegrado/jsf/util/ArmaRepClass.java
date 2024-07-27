/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.jsf.util;

import java.util.Date;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.TipoGamac;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;

/**
 *
 * @author msalinas
 */
public class ArmaRepClass {
    
    private Long id;
    private AmaCatalogo tipoArma;
    private AmaCatalogo marca;
    private String modelo;
    private String calibre;
    private String serie;
    private String nroRua;
    private String nroGuia;
    private String nroGuiaRef;
    
    private Long codigo;
    private String anaquel;
    private String fila;
    private String columna;
    private String observacion;
    private short cierreInventario;
    private short activo;
    private Date fechaCierre;
    private TipoGamac estadofuncionalId;
    private TipoGamac estadoconservacionId;
    private TipoGamac estadoserieId;
    private TipoGamac ambienteId;
    private SbPersona propietarioId;
    private SbDireccion almacenSucamecId;
    private TipoGamac situacionId;
    private TipoGamac materialCachaId;
    private TipoGamac estadoCachaId;
    private TipoGamac mecanismoId;
    private TipoGamac novedadCanonId;
    private TipoGamac exteriorId;
    private TipoGamac modalidadTiroId;
    private AmaModelos modeloId;
    private TipoGamac materialGuardamanoId;
    private TipoGamac materialCulataId;
    private TipoGamac materialCantoneraId;
    private TipoGamac materialEmpunaduraId;
    private TipoGamac estadoGuardamanoId;
    private TipoGamac estadoCantoneraId;
    private TipoGamac estadoCulataId;
    private TipoGamac estadoEmpunaduraId;
    private String codActaDeposito;
    
    private SbPersona personaPropietario;
    private int orden;
    private String nombrePersona;

    public AmaCatalogo getTipoArma() {
        return tipoArma;
    }

    public void setTipoArma(AmaCatalogo tipoArma) {
        this.tipoArma = tipoArma;
    }

    public AmaCatalogo getMarca() {
        return marca;
    }

    public void setMarca(AmaCatalogo marca) {
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

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNroRua() {
        return nroRua;
    }

    public void setNroRua(String nroRua) {
        this.nroRua = nroRua;
    }

    /**
     * @return the personaPropietario
     */
    public SbPersona getPersonaPropietario() {
        return personaPropietario;
    }

    /**
     * @param personaPropietario the personaPropietario to set
     */
    public void setPersonaPropietario(SbPersona personaPropietario) {
        this.personaPropietario = personaPropietario;
    }

    /**
     * @return the orden
     */
    public int getOrden() {
        return orden;
    }

    /**
     * @param orden the orden to set
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getNroGuia() {
        return nroGuia;
    }

    public void setNroGuia(String nroGuia) {
        this.nroGuia = nroGuia;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNroGuiaRef() {
        return nroGuiaRef;
    }

    public void setNroGuiaRef(String nroGuiaRef) {
        this.nroGuiaRef = nroGuiaRef;
    }
   
    /**
     * @return the codigo
     */
    public Long getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the anaquel
     */
    public String getAnaquel() {
        return anaquel;
    }

    /**
     * @param anaquel the anaquel to set
     */
    public void setAnaquel(String anaquel) {
        this.anaquel = anaquel;
    }
    
    /**
     * @return the fila
     */
    public String getFila() {
        return fila;
    }

    /**
     * @param fila the fila to set
     */
    public void setFila(String fila) {
        this.fila = fila;
    }

    /**
     * @return the columna
     */
    public String getColumna() {
        return columna;
    }

    /**
     * @param columna the columna to set
     */
    public void setColumna(String columna) {
        this.columna = columna;
    }

    /**
     * @return the observacion
     */
    public String getObservacion() {
        return observacion;
    }

    /**
     * @param observacion the observacion to set
     */
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    /**
     * @return the cierreInventario
     */
    public short getCierreInventario() {
        return cierreInventario;
    }

    /**
     * @param cierreInventario the cierreInventario to set
     */
    public void setCierreInventario(short cierreInventario) {
        this.cierreInventario = cierreInventario;
    }

    /**
     * @return the activo
     */
    public short getActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(short activo) {
        this.activo = activo;
    }

    /**
     * @return the fechaCierre
     */
    public Date getFechaCierre() {
        return fechaCierre;
    }

    /**
     * @param fechaCierre the fechaCierre to set
     */
    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }
    
    /**
     * @return the estadofuncionalId
     */
    public TipoGamac getEstadofuncionalId() {
        return estadofuncionalId;
    }

    /**
     * @param estadofuncionalId the estadofuncionalId to set
     */
    public void setEstadofuncionalId(TipoGamac estadofuncionalId) {
        this.estadofuncionalId = estadofuncionalId;
    }

    /**
     * @return the estadoconservacionId
     */
    public TipoGamac getEstadoconservacionId() {
        return estadoconservacionId;
    }

    /**
     * @param estadoconservacionId the estadoconservacionId to set
     */
    public void setEstadoconservacionId(TipoGamac estadoconservacionId) {
        this.estadoconservacionId = estadoconservacionId;
    }

    /**
     * @return the estadoserieId
     */
    public TipoGamac getEstadoserieId() {
        return estadoserieId;
    }

    /**
     * @param estadoserieId the estadoserieId to set
     */
    public void setEstadoserieId(TipoGamac estadoserieId) {
        this.estadoserieId = estadoserieId;
    }

    /**
     * @return the ambienteId
     */
    public TipoGamac getAmbienteId() {
        return ambienteId;
    }

    /**
     * @param ambienteId the ambienteId to set
     */
    public void setAmbienteId(TipoGamac ambienteId) {
        this.ambienteId = ambienteId;
    }

    /**
     * @return the propietarioId
     */
    public SbPersona getPropietarioId() {
        return propietarioId;
    }

    /**
     * @param propietarioId the propietarioId to set
     */
    public void setPropietarioId(SbPersona propietarioId) {
        this.propietarioId = propietarioId;
    }

    /**
     * @return the almacenSucamecId
     */
    public SbDireccion getAlmacenSucamecId() {
        return almacenSucamecId;
    }

    /**
     * @param almacenSucamecId the almacenSucamecId to set
     */
    public void setAlmacenSucamecId(SbDireccion almacenSucamecId) {
        this.almacenSucamecId = almacenSucamecId;
    }

    /**
     * @return the situacionId
     */
    public TipoGamac getSituacionId() {
        return situacionId;
    }

    /**
     * @param situacionId the situacionId to set
     */
    public void setSituacionId(TipoGamac situacionId) {
        this.situacionId = situacionId;
    }

    /**
     * @return the materialCachaId
     */
    public TipoGamac getMaterialCachaId() {
        return materialCachaId;
    }

    /**
     * @param materialCachaId the materialCachaId to set
     */
    public void setMaterialCachaId(TipoGamac materialCachaId) {
        this.materialCachaId = materialCachaId;
    }

    /**
     * @return the estadoCachaId
     */
    public TipoGamac getEstadoCachaId() {
        return estadoCachaId;
    }

    /**
     * @param estadoCachaId the estadoCachaId to set
     */
    public void setEstadoCachaId(TipoGamac estadoCachaId) {
        this.estadoCachaId = estadoCachaId;
    }

    /**
     * @return the mecanismoId
     */
    public TipoGamac getMecanismoId() {
        return mecanismoId;
    }

    /**
     * @param mecanismoId the mecanismoId to set
     */
    public void setMecanismoId(TipoGamac mecanismoId) {
        this.mecanismoId = mecanismoId;
    }

    /**
     * @return the novedadCanonId
     */
    public TipoGamac getNovedadCanonId() {
        return novedadCanonId;
    }

    /**
     * @param novedadCanonId the novedadCanonId to set
     */
    public void setNovedadCanonId(TipoGamac novedadCanonId) {
        this.novedadCanonId = novedadCanonId;
    }

    /**
     * @return the exteriorId
     */
    public TipoGamac getExteriorId() {
        return exteriorId;
    }

    /**
     * @param exteriorId the exteriorId to set
     */
    public void setExteriorId(TipoGamac exteriorId) {
        this.exteriorId = exteriorId;
    }

    /**
     * @return the modalidadTiroId
     */
    public TipoGamac getModalidadTiroId() {
        return modalidadTiroId;
    }

    /**
     * @param modalidadTiroId the modalidadTiroId to set
     */
    public void setModalidadTiroId(TipoGamac modalidadTiroId) {
        this.modalidadTiroId = modalidadTiroId;
    }

    /**
     * @return the modeloId
     */
    public AmaModelos getModeloId() {
        return modeloId;
    }

    /**
     * @param modeloId the modeloId to set
     */
    public void setModeloId(AmaModelos modeloId) {
        this.modeloId = modeloId;
    }

    public TipoGamac getMaterialGuardamanoId() {
        return materialGuardamanoId;
    }

    public void setMaterialGuardamanoId(TipoGamac materialGuardamanoId) {
        this.materialGuardamanoId = materialGuardamanoId;
    }

    public TipoGamac getMaterialCulataId() {
        return materialCulataId;
    }

    public void setMaterialCulataId(TipoGamac materialCulataId) {
        this.materialCulataId = materialCulataId;
    }
    
    public TipoGamac getMaterialCantoneraId() {
        return materialCantoneraId;
    }

    public void setMaterialCantoneraId(TipoGamac materialCantoneraId) {
        this.materialCantoneraId = materialCantoneraId;
    }

    public TipoGamac getMaterialEmpunaduraId() {
        return materialEmpunaduraId;
    }

    public void setMaterialEmpunaduraId(TipoGamac materialEmpunaduraId) {
        this.materialEmpunaduraId = materialEmpunaduraId;
    }

    public TipoGamac getEstadoGuardamanoId() {
        return estadoGuardamanoId;
    }

    public void setEstadoGuardamanoId(TipoGamac estadoGuardamanoId) {
        this.estadoGuardamanoId = estadoGuardamanoId;
    }

    public TipoGamac getEstadoCantoneraId() {
        return estadoCantoneraId;
    }

    public TipoGamac getEstadoCulataId() {
        return estadoCulataId;
    }

    public void setEstadoCulataId(TipoGamac estadoCulataId) {
        this.estadoCulataId = estadoCulataId;
    }

    public TipoGamac getEstadoEmpunaduraId() {
        return estadoEmpunaduraId;
    }

    public void setEstadoEmpunaduraId(TipoGamac estadoEmpunaduraId) {
        this.estadoEmpunaduraId = estadoEmpunaduraId;
    }

    public String getCodActaDeposito() {
        return codActaDeposito;
    }

    public void setCodActaDeposito(String codActaDeposito) {
        this.codActaDeposito = codActaDeposito;
    }

    public void setEstadoCantoneraId(TipoGamac estadoCantoneraId) {
        this.estadoCantoneraId = estadoCantoneraId;
    }
    
}
