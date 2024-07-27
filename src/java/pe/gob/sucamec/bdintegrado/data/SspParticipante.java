/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author locador772.ogtic
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_PARTICIPANTE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspParticipante.findAll", query = "SELECT s FROM SspParticipante s"),
    @NamedQuery(name = "SspParticipante.findById", query = "SELECT s FROM SspParticipante s WHERE s.id = :id"),
    @NamedQuery(name = "SspParticipante.findByTipoParticipacion", query = "SELECT s FROM SspParticipante s WHERE s.tipoParticipacion = :tipoParticipacion"),
    @NamedQuery(name = "SspParticipante.findBySubGrupo", query = "SELECT s FROM SspParticipante s WHERE s.subGrupo = :subGrupo"),
    @NamedQuery(name = "SspParticipante.findByCargo", query = "SELECT s FROM SspParticipante s WHERE s.cargo = :cargo"),
    @NamedQuery(name = "SspParticipante.findByParticipacion", query = "SELECT s FROM SspParticipante s WHERE s.participacion = :participacion"),
    @NamedQuery(name = "SspParticipante.findByFecha", query = "SELECT s FROM SspParticipante s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspParticipante.findByActivo", query = "SELECT s FROM SspParticipante s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspParticipante.findByAudLogin", query = "SELECT s FROM SspParticipante s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspParticipante.findByAudNumIp", query = "SELECT s FROM SspParticipante s WHERE s.audNumIp = :audNumIp")})
public class SspParticipante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_PARTICIPANTE")
    @SequenceGenerator(name = "SEQ_SSP_PARTICIPANTE", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_PARTICIPANTE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "TIPO_PARTICIPACION")
    private Long tipoParticipacion;
    
    @Size(max = 200)
    @Column(name = "SUB_GRUPO")
    private String subGrupo;
    
    @Size(max = 200)
    @Column(name = "CARGO")
    private String cargo;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PARTICIPACION")
    private BigDecimal participacion;
    
    @Size(max = 20)
    @Column(name = "NOMBRE_ARCHIVO")
    private String nombreArchivo;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
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
    
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt personaId;
    
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;
    
    @Column(name = "APORTE_MONTO")
    private BigDecimal aporteMonto;

    @Column(name = "NUMERO_ACCION")
    private BigDecimal numeroAccion;
    
    @Column(name = "PORCENTAJE_ACCION")
    private BigDecimal porcentajeAccion;
    
    @JoinColumn(name = "EMPRESA_EXT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbEmpresaExtranjera empresaExtId;
    
    @JoinColumn(name = "TIPO_DOC", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoDoc;
    

    public SspParticipante() {
    }

    public SspParticipante(Long id) {
        this.id = id;
    }

    public SspParticipante(Long id, Date fecha, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fecha = fecha;
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

    public Long getTipoParticipacion() {
        return tipoParticipacion;
    }

    public void setTipoParticipacion(Long tipoParticipacion) {
        this.tipoParticipacion = tipoParticipacion;
    }

    public String getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(String subGrupo) {
        this.subGrupo = subGrupo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public BigDecimal getParticipacion() {
        return participacion;
    }

    public void setParticipacion(BigDecimal participacion) {
        this.participacion = participacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
    }


    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public BigDecimal getAporteMonto() {
        return aporteMonto;
    }

    public void setAporteMonto(BigDecimal aporteMonto) {
        this.aporteMonto = aporteMonto;
    }

    public BigDecimal getNumeroAccion() {
        return numeroAccion;
    }

    public void setNumeroAccion(BigDecimal numeroAccion) {
        this.numeroAccion = numeroAccion;
    }

    public BigDecimal getPorcentajeAccion() {
        return porcentajeAccion;
    }

    public void setPorcentajeAccion(BigDecimal porcentajeAccion) {
        this.porcentajeAccion = porcentajeAccion;
    }
    
    public SbEmpresaExtranjera getEmpresaExtId() {
        return empresaExtId;
    }

    public void setEmpresaExtId(SbEmpresaExtranjera empresaExtId) {
        this.empresaExtId = empresaExtId;
    }
     
    public TipoBaseGt getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoBaseGt tipoDoc) {
        this.tipoDoc = tipoDoc;
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
        if (!(object instanceof SspParticipante)) {
            return false;
        }
        SspParticipante other = (SspParticipante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspParticipante[ id=" + id + " ]";
    }
    
    public String getDatosCompletos() {
        if(personaId != null){
            if(personaId.getApePat()!=null && personaId.getApeMat()!=null && personaId.getNombres()!=null){
                return personaId.getApePat()+" "+ personaId.getApeMat()+ " "+ personaId.getNombres();
            }
            else if(personaId.getApePat()!=null   && personaId.getNombres()!=null){
                return personaId.getApePat()+ " "+ personaId.getNombres();
            }else if( personaId.getNombres()!=null){
                return personaId.getNombres();
            }else {
                return "Sin datos";
            }    
        }else{
            return "Sin datos";
        }
        
        
    }
}
