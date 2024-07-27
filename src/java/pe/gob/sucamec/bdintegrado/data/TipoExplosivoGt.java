/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mespinoza
 */
@Entity
@Table(name = "TIPO_EXPLOSIVO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoExplosivoGt.findAll", query = "SELECT t FROM TipoExplosivoGt t"),
    @NamedQuery(name = "TipoExplosivoGt.findById", query = "SELECT t FROM TipoExplosivoGt t WHERE t.id = :id"),
    @NamedQuery(name = "TipoExplosivoGt.findByNombre", query = "SELECT t FROM TipoExplosivoGt t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoExplosivoGt.findByAbreviatura", query = "SELECT t FROM TipoExplosivoGt t WHERE t.abreviatura = :abreviatura"),
    @NamedQuery(name = "TipoExplosivoGt.findByDescripcion", query = "SELECT t FROM TipoExplosivoGt t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TipoExplosivoGt.findByCodProg", query = "SELECT t FROM TipoExplosivoGt t WHERE t.codProg = :codProg"),
    @NamedQuery(name = "TipoExplosivoGt.findByOrden", query = "SELECT t FROM TipoExplosivoGt t WHERE t.orden = :orden"),
    @NamedQuery(name = "TipoExplosivoGt.findByActivo", query = "SELECT t FROM TipoExplosivoGt t WHERE t.activo = :activo"),
    @NamedQuery(name = "TipoExplosivoGt.findByAudLogin", query = "SELECT t FROM TipoExplosivoGt t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TipoExplosivoGt.findByAudNumIp", query = "SELECT t FROM TipoExplosivoGt t WHERE t.audNumIp = :audNumIp")})
public class TipoExplosivoGt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "COD_PROG")
    private String codProg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDEN")
    private short orden;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @ManyToMany(mappedBy = "tipoExplosivoList")
    private List<EppPirotecnico> eppPirotecnicoList;
    @ManyToMany(mappedBy = "tipoExplosivoList")
    private List<EppClasifPirotecnico> eppClasifPirotecnicoList;
    @ManyToMany(mappedBy = "tipoExplosivoList")
    private List<EppRegistro> eppRegistroList;
    @ManyToMany(mappedBy = "tipoExplosivoList")
    private List<EppCapacitacion> eppCapacitacionList;    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "viaId")
    private List<EppRegistroIntSal> eppRegistroIntSalList;
    @OneToMany(mappedBy = "tipoDespachoId")
    private List<EppRegistroIntSal> eppRegistroIntSalList1;
    @OneToMany(mappedBy = "tipoIntendenciaId")
    private List<EppRegistroIntSal> eppRegistroIntSalList2;
    @OneToMany(mappedBy = "tipoRegimenId")
    private List<EppRegistroIntSal> eppRegistroIntSalList3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPerdida")
    private List<EppLibroPerdida> eppLibroPerdidaList;
    @OneToMany(mappedBy = "tipocontratoId")
    private List<EppContratoAlqPolv> eppContratoAlqPolvList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<EppLibroEventos> eppLibroEventosList;
    @OneToMany(mappedBy = "entidadEmisora")
    private List<EppCom> eppComList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstado1er")
    private List<EppCom> eppComList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoCom")
    private List<EppCom> eppComList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstado2do")
    private List<EppCom> eppComList3;
    @OneToMany(mappedBy = "tipoTramite")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProceso")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstado")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList2;
    @OneToMany(mappedBy = "tipoModificatoria")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoTransporte")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList4;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEmisionId")
    private List<EppLicencia> eppLicenciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoLicenciaId")
    private List<EppLicencia> eppLicenciaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoCargoId")
    private List<EppLicencia> eppLicenciaList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoExplosivo")
    private List<EppPolvorinAmbientes> eppPolvorinAmbientesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoSegLocalId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoZonaPolvId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoTramiteId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoTechoId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList3;
    @OneToMany(mappedBy = "tipoClasifaPolvId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList4;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProtecInterna")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList5;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPisosId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList6;
    @OneToMany(mappedBy = "tipoPerimetroId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList7;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoParedesId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList8;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoMaterialId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList9;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoInspeccionId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList10;
    @OneToMany(mappedBy = "tipoClasifEspecialId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList11;
    @OneToMany(mappedBy = "tipoClasifbPolvId")
    private List<EppInspeccionPolvorin> eppInspeccionPolvorinList12;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoGuiaId")
    private List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoTransporteId")
    private List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<EppResolucion> eppResolucionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<EppProveedorCliente> eppProveedorClienteList;
    @OneToMany(mappedBy = "tipoId")
    private List<TipoExplosivoGt> tipoExplosivoList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<EppExplosivo> eppExplosivoList;
    @OneToMany(mappedBy = "tipoUbicacionId")
    private List<EppLugarUso> eppLugarUsoList;
    @OneToMany(mappedBy = "tipoLugarId")
    private List<EppLugarUso> eppLugarUsoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoUsoId")
    private List<EppClasifPirotecnico> eppClasifPirotecnicoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPirotecnicoId")
    private List<EppClasifPirotecnico> eppClasifPirotecnicoList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoActividadId")
    private List<EppCarne> eppCarneList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoContratoId")
    private List<EppDepositoContrato> eppDepositoContratoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDepositoId")
    private List<EppComercializacion> eppComercializacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoUsoId")
    private List<EppComercializacion> eppComercializacionList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProdpiroId")
    private List<EppComercializacion> eppComercializacionList2;
    @OneToMany(mappedBy = "tipoProdpiroId")
    private List<EppPirotecnico> eppPirotecnicoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "divgrucomOnuId")
    private List<EppPirotecnico> eppPirotecnicoList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numeroOnuId")
    private List<EppPirotecnico> eppPirotecnicoList3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProdpiroId")
    private List<EppEspectaculo> eppEspectaculoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEspectaculoId")
    private List<EppEspectaculo> eppEspectaculoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoUbicacionId")
    private List<EppEspectaculo> eppEspectaculoList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoMaterialId")
    private List<EppDepositoAmbientes> eppDepositoAmbientesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoLocalId")
    private List<EppTallerdeposito> eppTallerdepositoList;
    @OneToMany(mappedBy = "clasificacionId")
    private List<EppTallerdeposito> eppTallerdepositoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoUsoId")
    private List<EppTallerdeposito> eppTallerdepositoList2;
    @OneToMany(mappedBy = "tipoProdpiroId")
    private List<EppTallerdeposito> eppTallerdepositoList3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoZonaId")
    private List<EppTallerdeposito> eppTallerdepositoList4;
    @OneToMany(mappedBy = "condInmuebleId")
    private List<EppTallerdeposito> eppTallerdepositoList5;
    @OneToMany(mappedBy = "tipoDespachoId")
    private List<EppInternaSalida> eppInternaSalidaList;
    @OneToMany(mappedBy = "tipoRegimenId")
    private List<EppInternaSalida> eppInternaSalidaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "viaId")
    private List<EppInternaSalida> eppInternaSalidaList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRegistroId")
    private List<EppLibroSaldo> eppLibroSaldoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEventoId")
    private List<EppRegistroEvento> eppRegistroEventoList;
    @OneToMany(mappedBy = "tipoRegistroId")
    private List<EppLibroUsoDiario> eppLibroUsoDiarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstado")
    private List<EppLibroUsoDiario> eppLibroUsoDiarioList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<EppDocumento> eppDocumentoList;
    @OneToMany(mappedBy = "tipoModificatoria")
    private List<EppRegistro> eppRegistroList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private List<EppRegistro> eppRegistroList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRegistroId")
    private List<EppLibroMes> eppLibroMesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstado")
    private List<EppLibroMes> eppLibroMesList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<EppAlmacen> eppAlmacenList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRegistroId")
    private List<EppLibroDetalle> eppLibroDetalleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPropietarioPolvorinId")
    private List<EppPolvorin> eppPolvorinList;
    @OneToMany(mappedBy = "tipoCancelacion")
    private List<EppPolvorin> eppPolvorinList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstado")
    private List<EppLibro> eppLibroList;
    @OneToMany(mappedBy = "tipoCodTributoId")
    private List<EppComprobante> eppComprobanteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRegistroId")
    private List<EppComprobante> eppComprobanteList1;

    public TipoExplosivoGt() {
    }

    public TipoExplosivoGt(Long id) {
        this.id = id;
    }

    public TipoExplosivoGt(Long id, String nombre, String abreviatura, String descripcion, String codProg, short orden, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.descripcion = descripcion;
        this.codProg = codProg;
        this.orden = orden;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodProg() {
        return codProg;
    }

    public void setCodProg(String codProg) {
        this.codProg = codProg;
    }

    public short getOrden() {
        return orden;
    }

    public void setOrden(short orden) {
        this.orden = orden;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public String getAudLogin() {
        return audLogin;
    }

    public void setAudLogin(String audLogin) {
        this.audLogin = audLogin;
    }

    public String getAudNumIp() {
        return audNumIp;
    }

    public void setAudNumIp(String audNumIp) {
        this.audNumIp = audNumIp;
    }

    @XmlTransient
    public List<EppPirotecnico> getEppPirotecnicoList() {
        return eppPirotecnicoList;
    }

    public void setEppPirotecnicoList(List<EppPirotecnico> eppPirotecnicoList) {
        this.eppPirotecnicoList = eppPirotecnicoList;
    }

    @XmlTransient
    public List<EppClasifPirotecnico> getEppClasifPirotecnicoList() {
        return eppClasifPirotecnicoList;
    }

    public void setEppClasifPirotecnicoList(List<EppClasifPirotecnico> eppClasifPirotecnicoList) {
        this.eppClasifPirotecnicoList = eppClasifPirotecnicoList;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    @XmlTransient
    public List<EppCapacitacion> getEppCapacitacionList() {
        return eppCapacitacionList;
    }

    public void setEppCapacitacionList(List<EppCapacitacion> eppCapacitacionList) {
        this.eppCapacitacionList = eppCapacitacionList;
    }

    @XmlTransient
    public List<EppRegistroIntSal> getEppRegistroIntSalList() {
        return eppRegistroIntSalList;
    }

    public void setEppRegistroIntSalList(List<EppRegistroIntSal> eppRegistroIntSalList) {
        this.eppRegistroIntSalList = eppRegistroIntSalList;
    }

    @XmlTransient
    public List<EppRegistroIntSal> getEppRegistroIntSalList1() {
        return eppRegistroIntSalList1;
    }

    public void setEppRegistroIntSalList1(List<EppRegistroIntSal> eppRegistroIntSalList1) {
        this.eppRegistroIntSalList1 = eppRegistroIntSalList1;
    }

    @XmlTransient
    public List<EppRegistroIntSal> getEppRegistroIntSalList2() {
        return eppRegistroIntSalList2;
    }

    public void setEppRegistroIntSalList2(List<EppRegistroIntSal> eppRegistroIntSalList2) {
        this.eppRegistroIntSalList2 = eppRegistroIntSalList2;
    }

    @XmlTransient
    public List<EppRegistroIntSal> getEppRegistroIntSalList3() {
        return eppRegistroIntSalList3;
    }

    public void setEppRegistroIntSalList3(List<EppRegistroIntSal> eppRegistroIntSalList3) {
        this.eppRegistroIntSalList3 = eppRegistroIntSalList3;
    }

    @XmlTransient
    public List<EppLibroPerdida> getEppLibroPerdidaList() {
        return eppLibroPerdidaList;
    }

    public void setEppLibroPerdidaList(List<EppLibroPerdida> eppLibroPerdidaList) {
        this.eppLibroPerdidaList = eppLibroPerdidaList;
    }

    @XmlTransient
    public List<EppContratoAlqPolv> getEppContratoAlqPolvList() {
        return eppContratoAlqPolvList;
    }

    public void setEppContratoAlqPolvList(List<EppContratoAlqPolv> eppContratoAlqPolvList) {
        this.eppContratoAlqPolvList = eppContratoAlqPolvList;
    }

    @XmlTransient
    public List<EppLibroEventos> getEppLibroEventosList() {
        return eppLibroEventosList;
    }

    public void setEppLibroEventosList(List<EppLibroEventos> eppLibroEventosList) {
        this.eppLibroEventosList = eppLibroEventosList;
    }

    @XmlTransient
    public List<EppCom> getEppComList() {
        return eppComList;
    }

    public void setEppComList(List<EppCom> eppComList) {
        this.eppComList = eppComList;
    }

    @XmlTransient
    public List<EppCom> getEppComList1() {
        return eppComList1;
    }

    public void setEppComList1(List<EppCom> eppComList1) {
        this.eppComList1 = eppComList1;
    }

    @XmlTransient
    public List<EppCom> getEppComList2() {
        return eppComList2;
    }

    public void setEppComList2(List<EppCom> eppComList2) {
        this.eppComList2 = eppComList2;
    }

    @XmlTransient
    public List<EppCom> getEppComList3() {
        return eppComList3;
    }

    public void setEppComList3(List<EppCom> eppComList3) {
        this.eppComList3 = eppComList3;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList() {
        return eppRegistroGuiaTransitoList;
    }

    public void setEppRegistroGuiaTransitoList(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList) {
        this.eppRegistroGuiaTransitoList = eppRegistroGuiaTransitoList;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList1() {
        return eppRegistroGuiaTransitoList1;
    }

    public void setEppRegistroGuiaTransitoList1(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList1) {
        this.eppRegistroGuiaTransitoList1 = eppRegistroGuiaTransitoList1;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList2() {
        return eppRegistroGuiaTransitoList2;
    }

    public void setEppRegistroGuiaTransitoList2(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList2) {
        this.eppRegistroGuiaTransitoList2 = eppRegistroGuiaTransitoList2;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList3() {
        return eppRegistroGuiaTransitoList3;
    }

    public void setEppRegistroGuiaTransitoList3(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList3) {
        this.eppRegistroGuiaTransitoList3 = eppRegistroGuiaTransitoList3;
    }

    @XmlTransient
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList4() {
        return eppRegistroGuiaTransitoList4;
    }

    public void setEppRegistroGuiaTransitoList4(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList4) {
        this.eppRegistroGuiaTransitoList4 = eppRegistroGuiaTransitoList4;
    }

    @XmlTransient
    public List<EppLicencia> getEppLicenciaList() {
        return eppLicenciaList;
    }

    public void setEppLicenciaList(List<EppLicencia> eppLicenciaList) {
        this.eppLicenciaList = eppLicenciaList;
    }

    @XmlTransient
    public List<EppLicencia> getEppLicenciaList1() {
        return eppLicenciaList1;
    }

    public void setEppLicenciaList1(List<EppLicencia> eppLicenciaList1) {
        this.eppLicenciaList1 = eppLicenciaList1;
    }

    @XmlTransient
    public List<EppLicencia> getEppLicenciaList2() {
        return eppLicenciaList2;
    }

    public void setEppLicenciaList2(List<EppLicencia> eppLicenciaList2) {
        this.eppLicenciaList2 = eppLicenciaList2;
    }

    @XmlTransient
    public List<EppPolvorinAmbientes> getEppPolvorinAmbientesList() {
        return eppPolvorinAmbientesList;
    }

    public void setEppPolvorinAmbientesList(List<EppPolvorinAmbientes> eppPolvorinAmbientesList) {
        this.eppPolvorinAmbientesList = eppPolvorinAmbientesList;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList() {
        return eppInspeccionPolvorinList;
    }

    public void setEppInspeccionPolvorinList(List<EppInspeccionPolvorin> eppInspeccionPolvorinList) {
        this.eppInspeccionPolvorinList = eppInspeccionPolvorinList;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList1() {
        return eppInspeccionPolvorinList1;
    }

    public void setEppInspeccionPolvorinList1(List<EppInspeccionPolvorin> eppInspeccionPolvorinList1) {
        this.eppInspeccionPolvorinList1 = eppInspeccionPolvorinList1;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList2() {
        return eppInspeccionPolvorinList2;
    }

    public void setEppInspeccionPolvorinList2(List<EppInspeccionPolvorin> eppInspeccionPolvorinList2) {
        this.eppInspeccionPolvorinList2 = eppInspeccionPolvorinList2;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList3() {
        return eppInspeccionPolvorinList3;
    }

    public void setEppInspeccionPolvorinList3(List<EppInspeccionPolvorin> eppInspeccionPolvorinList3) {
        this.eppInspeccionPolvorinList3 = eppInspeccionPolvorinList3;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList4() {
        return eppInspeccionPolvorinList4;
    }

    public void setEppInspeccionPolvorinList4(List<EppInspeccionPolvorin> eppInspeccionPolvorinList4) {
        this.eppInspeccionPolvorinList4 = eppInspeccionPolvorinList4;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList5() {
        return eppInspeccionPolvorinList5;
    }

    public void setEppInspeccionPolvorinList5(List<EppInspeccionPolvorin> eppInspeccionPolvorinList5) {
        this.eppInspeccionPolvorinList5 = eppInspeccionPolvorinList5;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList6() {
        return eppInspeccionPolvorinList6;
    }

    public void setEppInspeccionPolvorinList6(List<EppInspeccionPolvorin> eppInspeccionPolvorinList6) {
        this.eppInspeccionPolvorinList6 = eppInspeccionPolvorinList6;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList7() {
        return eppInspeccionPolvorinList7;
    }

    public void setEppInspeccionPolvorinList7(List<EppInspeccionPolvorin> eppInspeccionPolvorinList7) {
        this.eppInspeccionPolvorinList7 = eppInspeccionPolvorinList7;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList8() {
        return eppInspeccionPolvorinList8;
    }

    public void setEppInspeccionPolvorinList8(List<EppInspeccionPolvorin> eppInspeccionPolvorinList8) {
        this.eppInspeccionPolvorinList8 = eppInspeccionPolvorinList8;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList9() {
        return eppInspeccionPolvorinList9;
    }

    public void setEppInspeccionPolvorinList9(List<EppInspeccionPolvorin> eppInspeccionPolvorinList9) {
        this.eppInspeccionPolvorinList9 = eppInspeccionPolvorinList9;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList10() {
        return eppInspeccionPolvorinList10;
    }

    public void setEppInspeccionPolvorinList10(List<EppInspeccionPolvorin> eppInspeccionPolvorinList10) {
        this.eppInspeccionPolvorinList10 = eppInspeccionPolvorinList10;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList11() {
        return eppInspeccionPolvorinList11;
    }

    public void setEppInspeccionPolvorinList11(List<EppInspeccionPolvorin> eppInspeccionPolvorinList11) {
        this.eppInspeccionPolvorinList11 = eppInspeccionPolvorinList11;
    }

    @XmlTransient
    public List<EppInspeccionPolvorin> getEppInspeccionPolvorinList12() {
        return eppInspeccionPolvorinList12;
    }

    public void setEppInspeccionPolvorinList12(List<EppInspeccionPolvorin> eppInspeccionPolvorinList12) {
        this.eppInspeccionPolvorinList12 = eppInspeccionPolvorinList12;
    }

    @XmlTransient
    public List<EppGuiaTransitoPiro> getEppGuiaTransitoPiroList() {
        return eppGuiaTransitoPiroList;
    }

    public void setEppGuiaTransitoPiroList(List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList) {
        this.eppGuiaTransitoPiroList = eppGuiaTransitoPiroList;
    }

    @XmlTransient
    public List<EppGuiaTransitoPiro> getEppGuiaTransitoPiroList1() {
        return eppGuiaTransitoPiroList1;
    }

    public void setEppGuiaTransitoPiroList1(List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList1) {
        this.eppGuiaTransitoPiroList1 = eppGuiaTransitoPiroList1;
    }

    @XmlTransient
    public List<EppResolucion> getEppResolucionList() {
        return eppResolucionList;
    }

    public void setEppResolucionList(List<EppResolucion> eppResolucionList) {
        this.eppResolucionList = eppResolucionList;
    }

    @XmlTransient
    public List<EppProveedorCliente> getEppProveedorClienteList() {
        return eppProveedorClienteList;
    }

    public void setEppProveedorClienteList(List<EppProveedorCliente> eppProveedorClienteList) {
        this.eppProveedorClienteList = eppProveedorClienteList;
    }

    @XmlTransient
    public List<TipoExplosivoGt> getTipoExplosivoList() {
        return tipoExplosivoList;
    }

    public void setTipoExplosivoList(List<TipoExplosivoGt> tipoExplosivoList) {
        this.tipoExplosivoList = tipoExplosivoList;
    }

    public TipoExplosivoGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoExplosivoGt tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public List<EppExplosivo> getEppExplosivoList() {
        return eppExplosivoList;
    }

    public void setEppExplosivoList(List<EppExplosivo> eppExplosivoList) {
        this.eppExplosivoList = eppExplosivoList;
    }

    @XmlTransient
    public List<EppLugarUso> getEppLugarUsoList() {
        return eppLugarUsoList;
    }

    public void setEppLugarUsoList(List<EppLugarUso> eppLugarUsoList) {
        this.eppLugarUsoList = eppLugarUsoList;
    }

    @XmlTransient
    public List<EppLugarUso> getEppLugarUsoList1() {
        return eppLugarUsoList1;
    }

    public void setEppLugarUsoList1(List<EppLugarUso> eppLugarUsoList1) {
        this.eppLugarUsoList1 = eppLugarUsoList1;
    }

    @XmlTransient
    public List<EppClasifPirotecnico> getEppClasifPirotecnicoList1() {
        return eppClasifPirotecnicoList1;
    }

    public void setEppClasifPirotecnicoList1(List<EppClasifPirotecnico> eppClasifPirotecnicoList1) {
        this.eppClasifPirotecnicoList1 = eppClasifPirotecnicoList1;
    }

    @XmlTransient
    public List<EppClasifPirotecnico> getEppClasifPirotecnicoList2() {
        return eppClasifPirotecnicoList2;
    }

    public void setEppClasifPirotecnicoList2(List<EppClasifPirotecnico> eppClasifPirotecnicoList2) {
        this.eppClasifPirotecnicoList2 = eppClasifPirotecnicoList2;
    }

    @XmlTransient
    public List<EppCarne> getEppCarneList() {
        return eppCarneList;
    }

    public void setEppCarneList(List<EppCarne> eppCarneList) {
        this.eppCarneList = eppCarneList;
    }

    @XmlTransient
    public List<EppDepositoContrato> getEppDepositoContratoList() {
        return eppDepositoContratoList;
    }

    public void setEppDepositoContratoList(List<EppDepositoContrato> eppDepositoContratoList) {
        this.eppDepositoContratoList = eppDepositoContratoList;
    }

    @XmlTransient
    public List<EppComercializacion> getEppComercializacionList() {
        return eppComercializacionList;
    }

    public void setEppComercializacionList(List<EppComercializacion> eppComercializacionList) {
        this.eppComercializacionList = eppComercializacionList;
    }

    @XmlTransient
    public List<EppComercializacion> getEppComercializacionList1() {
        return eppComercializacionList1;
    }

    public void setEppComercializacionList1(List<EppComercializacion> eppComercializacionList1) {
        this.eppComercializacionList1 = eppComercializacionList1;
    }

    @XmlTransient
    public List<EppComercializacion> getEppComercializacionList2() {
        return eppComercializacionList2;
    }

    public void setEppComercializacionList2(List<EppComercializacion> eppComercializacionList2) {
        this.eppComercializacionList2 = eppComercializacionList2;
    }

    @XmlTransient
    public List<EppPirotecnico> getEppPirotecnicoList1() {
        return eppPirotecnicoList1;
    }

    public void setEppPirotecnicoList1(List<EppPirotecnico> eppPirotecnicoList1) {
        this.eppPirotecnicoList1 = eppPirotecnicoList1;
    }

    @XmlTransient
    public List<EppPirotecnico> getEppPirotecnicoList2() {
        return eppPirotecnicoList2;
    }

    public void setEppPirotecnicoList2(List<EppPirotecnico> eppPirotecnicoList2) {
        this.eppPirotecnicoList2 = eppPirotecnicoList2;
    }

    @XmlTransient
    public List<EppPirotecnico> getEppPirotecnicoList3() {
        return eppPirotecnicoList3;
    }

    public void setEppPirotecnicoList3(List<EppPirotecnico> eppPirotecnicoList3) {
        this.eppPirotecnicoList3 = eppPirotecnicoList3;
    }

    @XmlTransient
    public List<EppEspectaculo> getEppEspectaculoList() {
        return eppEspectaculoList;
    }

    public void setEppEspectaculoList(List<EppEspectaculo> eppEspectaculoList) {
        this.eppEspectaculoList = eppEspectaculoList;
    }

    @XmlTransient
    public List<EppEspectaculo> getEppEspectaculoList1() {
        return eppEspectaculoList1;
    }

    public void setEppEspectaculoList1(List<EppEspectaculo> eppEspectaculoList1) {
        this.eppEspectaculoList1 = eppEspectaculoList1;
    }

    @XmlTransient
    public List<EppEspectaculo> getEppEspectaculoList2() {
        return eppEspectaculoList2;
    }

    public void setEppEspectaculoList2(List<EppEspectaculo> eppEspectaculoList2) {
        this.eppEspectaculoList2 = eppEspectaculoList2;
    }

    @XmlTransient
    public List<EppDepositoAmbientes> getEppDepositoAmbientesList() {
        return eppDepositoAmbientesList;
    }

    public void setEppDepositoAmbientesList(List<EppDepositoAmbientes> eppDepositoAmbientesList) {
        this.eppDepositoAmbientesList = eppDepositoAmbientesList;
    }

    @XmlTransient
    public List<EppTallerdeposito> getEppTallerdepositoList() {
        return eppTallerdepositoList;
    }

    public void setEppTallerdepositoList(List<EppTallerdeposito> eppTallerdepositoList) {
        this.eppTallerdepositoList = eppTallerdepositoList;
    }

    @XmlTransient
    public List<EppTallerdeposito> getEppTallerdepositoList1() {
        return eppTallerdepositoList1;
    }

    public void setEppTallerdepositoList1(List<EppTallerdeposito> eppTallerdepositoList1) {
        this.eppTallerdepositoList1 = eppTallerdepositoList1;
    }

    @XmlTransient
    public List<EppTallerdeposito> getEppTallerdepositoList2() {
        return eppTallerdepositoList2;
    }

    public void setEppTallerdepositoList2(List<EppTallerdeposito> eppTallerdepositoList2) {
        this.eppTallerdepositoList2 = eppTallerdepositoList2;
    }

    @XmlTransient
    public List<EppTallerdeposito> getEppTallerdepositoList3() {
        return eppTallerdepositoList3;
    }

    public void setEppTallerdepositoList3(List<EppTallerdeposito> eppTallerdepositoList3) {
        this.eppTallerdepositoList3 = eppTallerdepositoList3;
    }

    @XmlTransient
    public List<EppTallerdeposito> getEppTallerdepositoList4() {
        return eppTallerdepositoList4;
    }

    public void setEppTallerdepositoList4(List<EppTallerdeposito> eppTallerdepositoList4) {
        this.eppTallerdepositoList4 = eppTallerdepositoList4;
    }

    @XmlTransient
    public List<EppTallerdeposito> getEppTallerdepositoList5() {
        return eppTallerdepositoList5;
    }

    public void setEppTallerdepositoList5(List<EppTallerdeposito> eppTallerdepositoList5) {
        this.eppTallerdepositoList5 = eppTallerdepositoList5;
    }

    @XmlTransient
    public List<EppInternaSalida> getEppInternaSalidaList() {
        return eppInternaSalidaList;
    }

    public void setEppInternaSalidaList(List<EppInternaSalida> eppInternaSalidaList) {
        this.eppInternaSalidaList = eppInternaSalidaList;
    }

    @XmlTransient
    public List<EppInternaSalida> getEppInternaSalidaList1() {
        return eppInternaSalidaList1;
    }

    public void setEppInternaSalidaList1(List<EppInternaSalida> eppInternaSalidaList1) {
        this.eppInternaSalidaList1 = eppInternaSalidaList1;
    }

    @XmlTransient
    public List<EppInternaSalida> getEppInternaSalidaList2() {
        return eppInternaSalidaList2;
    }

    public void setEppInternaSalidaList2(List<EppInternaSalida> eppInternaSalidaList2) {
        this.eppInternaSalidaList2 = eppInternaSalidaList2;
    }

    @XmlTransient
    public List<EppLibroSaldo> getEppLibroSaldoList() {
        return eppLibroSaldoList;
    }

    public void setEppLibroSaldoList(List<EppLibroSaldo> eppLibroSaldoList) {
        this.eppLibroSaldoList = eppLibroSaldoList;
    }

    @XmlTransient
    public List<EppRegistroEvento> getEppRegistroEventoList() {
        return eppRegistroEventoList;
    }

    public void setEppRegistroEventoList(List<EppRegistroEvento> eppRegistroEventoList) {
        this.eppRegistroEventoList = eppRegistroEventoList;
    }

    @XmlTransient
    public List<EppLibroUsoDiario> getEppLibroUsoDiarioList() {
        return eppLibroUsoDiarioList;
    }

    public void setEppLibroUsoDiarioList(List<EppLibroUsoDiario> eppLibroUsoDiarioList) {
        this.eppLibroUsoDiarioList = eppLibroUsoDiarioList;
    }

    @XmlTransient
    public List<EppLibroUsoDiario> getEppLibroUsoDiarioList1() {
        return eppLibroUsoDiarioList1;
    }

    public void setEppLibroUsoDiarioList1(List<EppLibroUsoDiario> eppLibroUsoDiarioList1) {
        this.eppLibroUsoDiarioList1 = eppLibroUsoDiarioList1;
    }

    @XmlTransient
    public List<EppDocumento> getEppDocumentoList() {
        return eppDocumentoList;
    }

    public void setEppDocumentoList(List<EppDocumento> eppDocumentoList) {
        this.eppDocumentoList = eppDocumentoList;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList1() {
        return eppRegistroList1;
    }

    public void setEppRegistroList1(List<EppRegistro> eppRegistroList1) {
        this.eppRegistroList1 = eppRegistroList1;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList2() {
        return eppRegistroList2;
    }

    public void setEppRegistroList2(List<EppRegistro> eppRegistroList2) {
        this.eppRegistroList2 = eppRegistroList2;
    }

    @XmlTransient
    public List<EppLibroMes> getEppLibroMesList() {
        return eppLibroMesList;
    }

    public void setEppLibroMesList(List<EppLibroMes> eppLibroMesList) {
        this.eppLibroMesList = eppLibroMesList;
    }

    @XmlTransient
    public List<EppLibroMes> getEppLibroMesList1() {
        return eppLibroMesList1;
    }

    public void setEppLibroMesList1(List<EppLibroMes> eppLibroMesList1) {
        this.eppLibroMesList1 = eppLibroMesList1;
    }

    @XmlTransient
    public List<EppAlmacen> getEppAlmacenList() {
        return eppAlmacenList;
    }

    public void setEppAlmacenList(List<EppAlmacen> eppAlmacenList) {
        this.eppAlmacenList = eppAlmacenList;
    }

    @XmlTransient
    public List<EppLibroDetalle> getEppLibroDetalleList() {
        return eppLibroDetalleList;
    }

    public void setEppLibroDetalleList(List<EppLibroDetalle> eppLibroDetalleList) {
        this.eppLibroDetalleList = eppLibroDetalleList;
    }

    @XmlTransient
    public List<EppPolvorin> getEppPolvorinList() {
        return eppPolvorinList;
    }

    public void setEppPolvorinList(List<EppPolvorin> eppPolvorinList) {
        this.eppPolvorinList = eppPolvorinList;
    }

    @XmlTransient
    public List<EppPolvorin> getEppPolvorinList1() {
        return eppPolvorinList1;
    }

    public void setEppPolvorinList1(List<EppPolvorin> eppPolvorinList1) {
        this.eppPolvorinList1 = eppPolvorinList1;
    }

    @XmlTransient
    public List<EppLibro> getEppLibroList() {
        return eppLibroList;
    }

    public void setEppLibroList(List<EppLibro> eppLibroList) {
        this.eppLibroList = eppLibroList;
    }

    @XmlTransient
    public List<EppComprobante> getEppComprobanteList() {
        return eppComprobanteList;
    }

    public void setEppComprobanteList(List<EppComprobante> eppComprobanteList) {
        this.eppComprobanteList = eppComprobanteList;
    }

    @XmlTransient
    public List<EppComprobante> getEppComprobanteList1() {
        return eppComprobanteList1;
    }

    public void setEppComprobanteList1(List<EppComprobante> eppComprobanteList1) {
        this.eppComprobanteList1 = eppComprobanteList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoExplosivoGt)) {
            return false;
        }
        TipoExplosivoGt other = (TipoExplosivoGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.TipoExplosivoGt[ id=" + id + " ]";
    }
    
}
