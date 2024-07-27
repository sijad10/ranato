/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.SbDistrito;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_INSPECCION_POLVORIN", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppInspeccionPolvorin.findAll", query = "SELECT e FROM EppInspeccionPolvorin e"),
    @NamedQuery(name = "EppInspeccionPolvorin.findById", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.id = :id"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByDescNombrePolv", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.descNombrePolv = :descNombrePolv"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByFechaInspeccion", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.fechaInspeccion = :fechaInspeccion"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByDireccionPolv", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.direccionPolv = :direccionPolv"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByRadioTransm", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.radioTransm = :radioTransm"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByTelefono", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.telefono = :telefono"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByAvisoPolvorin", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.avisoPolvorin = :avisoPolvorin"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByAvisoProhfumar", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.avisoProhfumar = :avisoProhfumar"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByRutaFile", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.rutaFile = :rutaFile"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByNumeroActa", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.numeroActa = :numeroActa"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondPuerta", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condPuerta = :condPuerta"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondVentana", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condVentana = :condVentana"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondVentila", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condVentila = :condVentila"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondIlumina", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condIlumina = :condIlumina"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondParihuela", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condParihuela = :condParihuela"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondPararrayo", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condPararrayo = :condPararrayo"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondElectrico", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condElectrico = :condElectrico"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondExterior", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condExterior = :condExterior"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondEmpotrado", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condEmpotrado = :condEmpotrado"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondTubos", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condTubos = :condTubos"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondPared", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condPared = :condPared"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondAltura", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condAltura = :condAltura"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByCondSepara", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.condSepara = :condSepara"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByMatDinamita", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.matDinamita = :matDinamita"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByMatEmulsion", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.matEmulsion = :matEmulsion"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByMatOtroExplo", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.matOtroExplo = :matOtroExplo"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByMatMecha", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.matMecha = :matMecha"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByMatFulmina", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.matFulmina = :matFulmina"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByMatCordon", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.matCordon = :matCordon"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByMatOtroAcceso", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.matOtroAcceso = :matOtroAcceso"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByMatAnfo", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.matAnfo = :matAnfo"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByMatNitrato", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.matNitrato = :matNitrato"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByMatCartilla", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.matCartilla = :matCartilla"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByObservaciones", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.observaciones = :observaciones"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByServicioRd", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.servicioRd = :servicioRd"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByActivo", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByAudLogin", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppInspeccionPolvorin.findByAudNumIp", query = "SELECT e FROM EppInspeccionPolvorin e WHERE e.audNumIp = :audNumIp")})
public class EppInspeccionPolvorin implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_INSPECCION_POLVORIN")
    @SequenceGenerator(name = "SEQ_EPP_INSPECCION_POLVORIN", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_INSPECCION_POLVORIN", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESC_NOMBRE_POLV")
    private String descNombrePolv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INSPECCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInspeccion;
    @Size(max = 500)
    @Column(name = "DIRECCION_POLV")
    private String direccionPolv;
    @Column(name = "RADIO_TRANSM")
    private Short radioTransm;
    @Column(name = "TELEFONO")
    private Short telefono;
    @Column(name = "AVISO_POLVORIN")
    private Short avisoPolvorin;
    @Column(name = "AVISO_PROHFUMAR")
    private Short avisoProhfumar;
    @Size(max = 500)
    @Column(name = "RUTA_FILE")
    private String rutaFile;
    @Size(max = 25)
    @Column(name = "NUMERO_ACTA")
    private String numeroActa;
    @Column(name = "COND_PUERTA")
    private Short condPuerta;
    @Column(name = "COND_VENTANA")
    private Short condVentana;
    @Column(name = "COND_VENTILA")
    private Short condVentila;
    @Column(name = "COND_ILUMINA")
    private Short condIlumina;
    @Column(name = "COND_PARIHUELA")
    private Short condParihuela;
    @Column(name = "COND_PARARRAYO")
    private Short condPararrayo;
    @Column(name = "COND_ELECTRICO")
    private Short condElectrico;
    @Column(name = "COND_EXTERIOR")
    private Short condExterior;
    @Column(name = "COND_EMPOTRADO")
    private Short condEmpotrado;
    @Column(name = "COND_TUBOS")
    private Short condTubos;
    @Column(name = "COND_PARED")
    private Short condPared;
    @Column(name = "COND_ALTURA")
    private Short condAltura;
    @Column(name = "COND_SEPARA")
    private Short condSepara;
    @Column(name = "MAT_DINAMITA")
    private Short matDinamita;
    @Column(name = "MAT_EMULSION")
    private Short matEmulsion;
    @Size(max = 100)
    @Column(name = "MAT_OTRO_EXPLO")
    private String matOtroExplo;
    @Column(name = "MAT_MECHA")
    private Short matMecha;
    @Column(name = "MAT_FULMINA")
    private Short matFulmina;
    @Column(name = "MAT_CORDON")
    private Short matCordon;
    @Size(max = 100)
    @Column(name = "MAT_OTRO_ACCESO")
    private String matOtroAcceso;
    @Column(name = "MAT_ANFO")
    private Short matAnfo;
    @Column(name = "MAT_NITRATO")
    private Short matNitrato;
    @Column(name = "MAT_CARTILLA")
    private Short matCartilla;
    @Size(max = 500)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Size(max = 50)
    @Column(name = "SERVICIO_RD")
    private String servicioRd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inspeccionPolvorinId")
    private List<EppPolvorinAmbientes> eppPolvorinAmbientesList;
    @JoinColumn(name = "TIPO_SEG_LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoSegLocalId;
    @JoinColumn(name = "TIPO_ZONA_POLV_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoZonaPolvId;
    @JoinColumn(name = "TIPO_TRAMITE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoTramiteId;
    @JoinColumn(name = "TIPO_TECHO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoTechoId;
    @JoinColumn(name = "TIPO_CLASIFA_POLV_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoClasifaPolvId;
    @JoinColumn(name = "TIPO_PROTEC_INTERNA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoProtecInterna;
    @JoinColumn(name = "INSPECTOR_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppInspector inspectorId;
    @JoinColumn(name = "POLVORIN_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppPolvorin polvorinId;
    @JoinColumn(name = "DISTRITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistrito distritoId;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona empresaId;
    @JoinColumn(name = "PERSEMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona persempresaId;
    @JoinColumn(name = "TIPO_PISOS_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoPisosId;
    @JoinColumn(name = "TIPO_PERIMETRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoPerimetroId;
    @JoinColumn(name = "TIPO_PAREDES_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoParedesId;
    @JoinColumn(name = "TIPO_MATERIAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoMaterialId;
    @JoinColumn(name = "TIPO_INSPECCION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoInspeccionId;
    @JoinColumn(name = "TIPO_CLASIF_ESPECIAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoClasifEspecialId;
    @JoinColumn(name = "TIPO_CLASIFB_POLV_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoClasifbPolvId;

    public EppInspeccionPolvorin() {
    }

    public EppInspeccionPolvorin(Long id) {
        this.id = id;
    }

    public EppInspeccionPolvorin(Long id, String descNombrePolv, Date fechaInspeccion, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.descNombrePolv = descNombrePolv;
        this.fechaInspeccion = fechaInspeccion;
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

    public String getDescNombrePolv() {
        return descNombrePolv;
    }

    public void setDescNombrePolv(String descNombrePolv) {
        this.descNombrePolv = descNombrePolv;
    }

    public Date getFechaInspeccion() {
        return fechaInspeccion;
    }

    public void setFechaInspeccion(Date fechaInspeccion) {
        this.fechaInspeccion = fechaInspeccion;
    }

    public String getDireccionPolv() {
        return direccionPolv;
    }

    public void setDireccionPolv(String direccionPolv) {
        this.direccionPolv = direccionPolv;
    }

    public Short getRadioTransm() {
        return radioTransm;
    }

    public void setRadioTransm(Short radioTransm) {
        this.radioTransm = radioTransm;
    }

    public Short getTelefono() {
        return telefono;
    }

    public void setTelefono(Short telefono) {
        this.telefono = telefono;
    }

    public Short getAvisoPolvorin() {
        return avisoPolvorin;
    }

    public void setAvisoPolvorin(Short avisoPolvorin) {
        this.avisoPolvorin = avisoPolvorin;
    }

    public Short getAvisoProhfumar() {
        return avisoProhfumar;
    }

    public void setAvisoProhfumar(Short avisoProhfumar) {
        this.avisoProhfumar = avisoProhfumar;
    }

    public String getRutaFile() {
        return rutaFile;
    }

    public void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    public String getNumeroActa() {
        return numeroActa;
    }

    public void setNumeroActa(String numeroActa) {
        this.numeroActa = numeroActa;
    }

    public Short getCondPuerta() {
        return condPuerta;
    }

    public void setCondPuerta(Short condPuerta) {
        this.condPuerta = condPuerta;
    }

    public Short getCondVentana() {
        return condVentana;
    }

    public void setCondVentana(Short condVentana) {
        this.condVentana = condVentana;
    }

    public Short getCondVentila() {
        return condVentila;
    }

    public void setCondVentila(Short condVentila) {
        this.condVentila = condVentila;
    }

    public Short getCondIlumina() {
        return condIlumina;
    }

    public void setCondIlumina(Short condIlumina) {
        this.condIlumina = condIlumina;
    }

    public Short getCondParihuela() {
        return condParihuela;
    }

    public void setCondParihuela(Short condParihuela) {
        this.condParihuela = condParihuela;
    }

    public Short getCondPararrayo() {
        return condPararrayo;
    }

    public void setCondPararrayo(Short condPararrayo) {
        this.condPararrayo = condPararrayo;
    }

    public Short getCondElectrico() {
        return condElectrico;
    }

    public void setCondElectrico(Short condElectrico) {
        this.condElectrico = condElectrico;
    }

    public Short getCondExterior() {
        return condExterior;
    }

    public void setCondExterior(Short condExterior) {
        this.condExterior = condExterior;
    }

    public Short getCondEmpotrado() {
        return condEmpotrado;
    }

    public void setCondEmpotrado(Short condEmpotrado) {
        this.condEmpotrado = condEmpotrado;
    }

    public Short getCondTubos() {
        return condTubos;
    }

    public void setCondTubos(Short condTubos) {
        this.condTubos = condTubos;
    }

    public Short getCondPared() {
        return condPared;
    }

    public void setCondPared(Short condPared) {
        this.condPared = condPared;
    }

    public Short getCondAltura() {
        return condAltura;
    }

    public void setCondAltura(Short condAltura) {
        this.condAltura = condAltura;
    }

    public Short getCondSepara() {
        return condSepara;
    }

    public void setCondSepara(Short condSepara) {
        this.condSepara = condSepara;
    }

    public Short getMatDinamita() {
        return matDinamita;
    }

    public void setMatDinamita(Short matDinamita) {
        this.matDinamita = matDinamita;
    }

    public Short getMatEmulsion() {
        return matEmulsion;
    }

    public void setMatEmulsion(Short matEmulsion) {
        this.matEmulsion = matEmulsion;
    }

    public String getMatOtroExplo() {
        return matOtroExplo;
    }

    public void setMatOtroExplo(String matOtroExplo) {
        this.matOtroExplo = matOtroExplo;
    }

    public Short getMatMecha() {
        return matMecha;
    }

    public void setMatMecha(Short matMecha) {
        this.matMecha = matMecha;
    }

    public Short getMatFulmina() {
        return matFulmina;
    }

    public void setMatFulmina(Short matFulmina) {
        this.matFulmina = matFulmina;
    }

    public Short getMatCordon() {
        return matCordon;
    }

    public void setMatCordon(Short matCordon) {
        this.matCordon = matCordon;
    }

    public String getMatOtroAcceso() {
        return matOtroAcceso;
    }

    public void setMatOtroAcceso(String matOtroAcceso) {
        this.matOtroAcceso = matOtroAcceso;
    }

    public Short getMatAnfo() {
        return matAnfo;
    }

    public void setMatAnfo(Short matAnfo) {
        this.matAnfo = matAnfo;
    }

    public Short getMatNitrato() {
        return matNitrato;
    }

    public void setMatNitrato(Short matNitrato) {
        this.matNitrato = matNitrato;
    }

    public Short getMatCartilla() {
        return matCartilla;
    }

    public void setMatCartilla(Short matCartilla) {
        this.matCartilla = matCartilla;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getServicioRd() {
        return servicioRd;
    }

    public void setServicioRd(String servicioRd) {
        this.servicioRd = servicioRd;
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
    public List<EppPolvorinAmbientes> getEppPolvorinAmbientesList() {
        return eppPolvorinAmbientesList;
    }

    public void setEppPolvorinAmbientesList(List<EppPolvorinAmbientes> eppPolvorinAmbientesList) {
        this.eppPolvorinAmbientesList = eppPolvorinAmbientesList;
    }

    public TipoExplosivoGt getTipoSegLocalId() {
        return tipoSegLocalId;
    }

    public void setTipoSegLocalId(TipoExplosivoGt tipoSegLocalId) {
        this.tipoSegLocalId = tipoSegLocalId;
    }

    public TipoExplosivoGt getTipoZonaPolvId() {
        return tipoZonaPolvId;
    }

    public void setTipoZonaPolvId(TipoExplosivoGt tipoZonaPolvId) {
        this.tipoZonaPolvId = tipoZonaPolvId;
    }

    public TipoExplosivoGt getTipoTramiteId() {
        return tipoTramiteId;
    }

    public void setTipoTramiteId(TipoExplosivoGt tipoTramiteId) {
        this.tipoTramiteId = tipoTramiteId;
    }

    public TipoExplosivoGt getTipoTechoId() {
        return tipoTechoId;
    }

    public void setTipoTechoId(TipoExplosivoGt tipoTechoId) {
        this.tipoTechoId = tipoTechoId;
    }

    public TipoExplosivoGt getTipoClasifaPolvId() {
        return tipoClasifaPolvId;
    }

    public void setTipoClasifaPolvId(TipoExplosivoGt tipoClasifaPolvId) {
        this.tipoClasifaPolvId = tipoClasifaPolvId;
    }

    public TipoExplosivoGt getTipoProtecInterna() {
        return tipoProtecInterna;
    }

    public void setTipoProtecInterna(TipoExplosivoGt tipoProtecInterna) {
        this.tipoProtecInterna = tipoProtecInterna;
    }

    public EppInspector getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(EppInspector inspectorId) {
        this.inspectorId = inspectorId;
    }

    public EppPolvorin getPolvorinId() {
        return polvorinId;
    }

    public void setPolvorinId(EppPolvorin polvorinId) {
        this.polvorinId = polvorinId;
    }

    public SbDistrito getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(SbDistrito distritoId) {
        this.distritoId = distritoId;
    }

    public SbPersona getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersona empresaId) {
        this.empresaId = empresaId;
    }

    public SbPersona getPersempresaId() {
        return persempresaId;
    }

    public void setPersempresaId(SbPersona persempresaId) {
        this.persempresaId = persempresaId;
    }

    public TipoExplosivoGt getTipoPisosId() {
        return tipoPisosId;
    }

    public void setTipoPisosId(TipoExplosivoGt tipoPisosId) {
        this.tipoPisosId = tipoPisosId;
    }

    public TipoExplosivoGt getTipoPerimetroId() {
        return tipoPerimetroId;
    }

    public void setTipoPerimetroId(TipoExplosivoGt tipoPerimetroId) {
        this.tipoPerimetroId = tipoPerimetroId;
    }

    public TipoExplosivoGt getTipoParedesId() {
        return tipoParedesId;
    }

    public void setTipoParedesId(TipoExplosivoGt tipoParedesId) {
        this.tipoParedesId = tipoParedesId;
    }

    public TipoExplosivoGt getTipoMaterialId() {
        return tipoMaterialId;
    }

    public void setTipoMaterialId(TipoExplosivoGt tipoMaterialId) {
        this.tipoMaterialId = tipoMaterialId;
    }

    public TipoExplosivoGt getTipoInspeccionId() {
        return tipoInspeccionId;
    }

    public void setTipoInspeccionId(TipoExplosivoGt tipoInspeccionId) {
        this.tipoInspeccionId = tipoInspeccionId;
    }

    public TipoExplosivoGt getTipoClasifEspecialId() {
        return tipoClasifEspecialId;
    }

    public void setTipoClasifEspecialId(TipoExplosivoGt tipoClasifEspecialId) {
        this.tipoClasifEspecialId = tipoClasifEspecialId;
    }

    public TipoExplosivoGt getTipoClasifbPolvId() {
        return tipoClasifbPolvId;
    }

    public void setTipoClasifbPolvId(TipoExplosivoGt tipoClasifbPolvId) {
        this.tipoClasifbPolvId = tipoClasifbPolvId;
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
        if (!(object instanceof EppInspeccionPolvorin)) {
            return false;
        }
        EppInspeccionPolvorin other = (EppInspeccionPolvorin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppInspeccionPolvorin[ id=" + id + " ]";
    }
    
}
