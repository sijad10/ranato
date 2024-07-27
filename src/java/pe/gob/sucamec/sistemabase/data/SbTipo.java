/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.bdintegrado.data.SbEmpresaExtranjera;
import pe.gob.sucamec.encuesta.data.SbEncuesta;
import pe.gob.sucamec.encuesta.data.SbPregunta;
import pe.gob.sucamec.encuesta.data.SbRespuesta;
import pe.gob.sucamec.notificacion.data.NeArchivo;
import pe.gob.sucamec.notificacion.data.NeDocumento;
import pe.gob.sucamec.notificacion.data.NeEvento;
import pe.gob.sucamec.sel.epp.data.PuertoAduanero;
import pe.gob.sucamec.sel.epp.data.Registro;
import pe.gob.sucamec.sel.gamac.data.GamacSbPersona;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "TIPO_BASE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbTipo.findAll", query = "SELECT s FROM SbTipo s"),
    @NamedQuery(name = "SbTipo.findById", query = "SELECT s FROM SbTipo s WHERE s.id = :id"),
    @NamedQuery(name = "SbTipo.findByNombre", query = "SELECT s FROM SbTipo s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbTipo.findByAbreviatura", query = "SELECT s FROM SbTipo s WHERE s.abreviatura = :abreviatura"),
    @NamedQuery(name = "SbTipo.findByDescripcion", query = "SELECT s FROM SbTipo s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SbTipo.findByCodProg", query = "SELECT s FROM SbTipo s WHERE s.codProg = :codProg"),
    @NamedQuery(name = "SbTipo.findByOrden", query = "SELECT s FROM SbTipo s WHERE s.orden = :orden"),
    @NamedQuery(name = "SbTipo.findByActivo", query = "SELECT s FROM SbTipo s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbTipo.findByAudLogin", query = "SELECT s FROM SbTipo s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbTipo.findByAudNumIp", query = "SELECT s FROM SbTipo s WHERE s.audNumIp = :audNumIp")})
public class SbTipo implements Serializable {
 
    @OneToMany(mappedBy = "ocupacionId")
    private List<GamacSbPersona> gamacSbPersonaList;
    @OneToMany(mappedBy = "instituLabId")
    private List<GamacSbPersona> gamacSbPersonaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<GamacSbPersona> gamacSbPersonaList2;
    @OneToMany(mappedBy = "tipoDoc")
    private List<GamacSbPersona> gamacSbPersonaList3;
    @OneToMany(mappedBy = "generoId")
    private List<GamacSbPersona> gamacSbPersonaList4;
    @OneToMany(mappedBy = "estCivilId")
    private List<GamacSbPersona> gamacSbPersonaList5;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "viaId")
    private List<PuertoAduanero> puertoAduaneroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRegId")
    private List<Registro> registroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProId")
    private List<Registro> registroList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoOpeId")
    private List<Registro> registroList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<NeArchivo> neArchivoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<NeArchivo> neArchivoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<NeDocumento> neDocumentoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<NeDocumento> neDocumentoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "areaId")
    private List<NeDocumento> neDocumentoList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<NeEvento> neEventoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<NeEvento> neEventoList1;
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_BASE")
    @SequenceGenerator(name = "SEQ_TIPO_BASE", schema = "BDINTEGRADO", sequenceName = "SEQ_TIPO_BASE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 50)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "COD_PROG")
    private String codProg;
    @Column(name = "ORDEN")
    private Integer orden;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zonaId")
    private List<SbDireccion> sbDireccionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbDireccion> sbDireccionList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "viaId")
    private List<SbDireccion> sbDireccionList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbRelacionPersona> sbRelacionPersonaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbValidacionWeb> sbValidacionWebList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbLogUsuario> sbLogUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAutenId")
    private List<SbUsuario> sbUsuarioList;
    @OneToMany(mappedBy = "areaId")
    private List<SbUsuario> sbUsuarioList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbUsuario> sbUsuarioList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbMedioContacto> sbMedioContactoList;
    @OneToMany(mappedBy = "estCivilId")
    private List<SbPersona> sbPersonaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbPersona> sbPersonaList1;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
//    private List<SbEncuesta> sbEncuestaList;
//    @OneToMany(mappedBy = "alternativaId")
//    private List<SbRespuesta> sbRespuestaList;
    @OneToMany(mappedBy = "generoId")
    private List<SbPersona> sbPersonaList2;
    @OneToMany(mappedBy = "tipoDoc")
    private List<SbPersona> sbPersonaList3;
    @OneToMany(mappedBy = "tipoId")
    private List<SbTipo> sbTipoList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbTipo tipoId;
    @JoinTable(schema = "BDINTEGRADO", name = "SB_ALTERNATIVA", joinColumns = {
        @JoinColumn(name = "ALTERNATIVA_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PREGUNTA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbPregunta> sbPreguntaList;

    public SbTipo() {
    }

    public SbTipo(Long id) {
        this.id = id;
    }

    public SbTipo(Long id, String nombre, String codProg, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.codProg = codProg;
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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
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
    public List<SbDireccion> getSbDireccionList() {
        return sbDireccionList;
    }

    public void setSbDireccionList(List<SbDireccion> sbDireccionList) {
        this.sbDireccionList = sbDireccionList;
    }

    @XmlTransient
    public List<SbDireccion> getSbDireccionList1() {
        return sbDireccionList1;
    }

    public void setSbDireccionList1(List<SbDireccion> sbDireccionList1) {
        this.sbDireccionList1 = sbDireccionList1;
    }

    @XmlTransient
    public List<SbDireccion> getSbDireccionList2() {
        return sbDireccionList2;
    }

    public void setSbDireccionList2(List<SbDireccion> sbDireccionList2) {
        this.sbDireccionList2 = sbDireccionList2;
    }

    @XmlTransient
    public List<SbRelacionPersona> getSbRelacionPersonaList() {
        return sbRelacionPersonaList;
    }

    public void setSbRelacionPersonaList(List<SbRelacionPersona> sbRelacionPersonaList) {
        this.sbRelacionPersonaList = sbRelacionPersonaList;
    }

    @XmlTransient
    public List<SbValidacionWeb> getSbValidacionWebList() {
        return sbValidacionWebList;
    }

    public void setSbValidacionWebList(List<SbValidacionWeb> sbValidacionWebList) {
        this.sbValidacionWebList = sbValidacionWebList;
    }

    @XmlTransient
    public List<SbLogUsuario> getSbLogUsuarioList() {
        return sbLogUsuarioList;
    }

    public void setSbLogUsuarioList(List<SbLogUsuario> sbLogUsuarioList) {
        this.sbLogUsuarioList = sbLogUsuarioList;
    }

    @XmlTransient
    public List<SbUsuario> getSbUsuarioList() {
        return sbUsuarioList;
    }

    public void setSbUsuarioList(List<SbUsuario> sbUsuarioList) {
        this.sbUsuarioList = sbUsuarioList;
    }

    @XmlTransient
    public List<SbUsuario> getSbUsuarioList1() {
        return sbUsuarioList1;
    }

    public void setSbUsuarioList1(List<SbUsuario> sbUsuarioList1) {
        this.sbUsuarioList1 = sbUsuarioList1;
    }

    @XmlTransient
    public List<SbUsuario> getSbUsuarioList2() {
        return sbUsuarioList2;
    }

    public void setSbUsuarioList2(List<SbUsuario> sbUsuarioList2) {
        this.sbUsuarioList2 = sbUsuarioList2;
    }

    @XmlTransient
    public List<SbMedioContacto> getSbMedioContactoList() {
        return sbMedioContactoList;
    }

    public void setSbMedioContactoList(List<SbMedioContacto> sbMedioContactoList) {
        this.sbMedioContactoList = sbMedioContactoList;
    }

    @XmlTransient
    public List<SbPersona> getSbPersonaList() {
        return sbPersonaList;
    }

    public void setSbPersonaList(List<SbPersona> sbPersonaList) {
        this.sbPersonaList = sbPersonaList;
    }

    @XmlTransient
    public List<SbPersona> getSbPersonaList1() {
        return sbPersonaList1;
    }

    public void setSbPersonaList1(List<SbPersona> sbPersonaList1) {
        this.sbPersonaList1 = sbPersonaList1;
    }

    @XmlTransient
    public List<SbPersona> getSbPersonaList2() {
        return sbPersonaList2;
    }

    public void setSbPersonaList2(List<SbPersona> sbPersonaList2) {
        this.sbPersonaList2 = sbPersonaList2;
    }

    @XmlTransient
    public List<SbPersona> getSbPersonaList3() {
        return sbPersonaList3;
    }

    public void setSbPersonaList3(List<SbPersona> sbPersonaList3) {
        this.sbPersonaList3 = sbPersonaList3;
    }

    @XmlTransient
    public List<SbTipo> getSbTipoList() {
        return sbTipoList;
    }

    public void setSbTipoList(List<SbTipo> sbTipoList) {
        this.sbTipoList = sbTipoList;
    }
    
//    @XmlTransient
//    public List<SbEncuesta> getSbEncuestaList() {
//        return sbEncuestaList;
//    }
//
//    public void setSbEncuestaList(List<SbEncuesta> sbEncuestaList) {
//        this.sbEncuestaList = sbEncuestaList;
//    }
//    
//    @XmlTransient
//    public List<SbRespuesta> getSbRespuestaList() {
//        return sbRespuestaList;
//    }
//
//    public void setSbRespuestaList(List<SbRespuesta> sbRespuestaList) {
//        this.sbRespuestaList = sbRespuestaList;
//    }

    public SbTipo getTipoId() {
        return tipoId;
    }

    public void setTipoId(SbTipo tipoId) {
        this.tipoId = tipoId;
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
        if (!(object instanceof SbTipo)) {
            return false;
        }
        SbTipo other = (SbTipo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbTipo[ id=" + id + " ]";
    }

    @XmlTransient
    public List<NeArchivo> getNeArchivoList() {
        return neArchivoList;
    }

    public void setNeArchivoList(List<NeArchivo> neArchivoList) {
        this.neArchivoList = neArchivoList;
    }

    @XmlTransient
    public List<NeArchivo> getNeArchivoList1() {
        return neArchivoList1;
    }

    public void setNeArchivoList1(List<NeArchivo> neArchivoList1) {
        this.neArchivoList1 = neArchivoList1;
    }

    @XmlTransient
    public List<NeDocumento> getNeDocumentoList() {
        return neDocumentoList;
    }

    public void setNeDocumentoList(List<NeDocumento> neDocumentoList) {
        this.neDocumentoList = neDocumentoList;
    }

    @XmlTransient
    public List<NeDocumento> getNeDocumentoList1() {
        return neDocumentoList1;
    }

    public void setNeDocumentoList1(List<NeDocumento> neDocumentoList1) {
        this.neDocumentoList1 = neDocumentoList1;
    }

    @XmlTransient
    public List<NeDocumento> getNeDocumentoList2() {
        return neDocumentoList2;
    }

    public void setNeDocumentoList2(List<NeDocumento> neDocumentoList2) {
        this.neDocumentoList2 = neDocumentoList2;
    }

    @XmlTransient
    public List<NeEvento> getNeEventoList() {
        return neEventoList;
    }

    public void setNeEventoList(List<NeEvento> neEventoList) {
        this.neEventoList = neEventoList;
    }

    @XmlTransient
    public List<NeEvento> getNeEventoList1() {
        return neEventoList1;
    }

    public void setNeEventoList1(List<NeEvento> neEventoList1) {
        this.neEventoList1 = neEventoList1;
    }

    @XmlTransient
    public List<PuertoAduanero> getPuertoAduaneroList() {
        return puertoAduaneroList;
    }

    public void setPuertoAduaneroList(List<PuertoAduanero> puertoAduaneroList) {
        this.puertoAduaneroList = puertoAduaneroList;
    }

    @XmlTransient
    public List<Registro> getRegistroList() {
        return registroList;
    }

    public void setRegistroList(List<Registro> registroList) {
        this.registroList = registroList;
    }

    @XmlTransient
    public List<Registro> getRegistroList1() {
        return registroList1;
    }

    public void setRegistroList1(List<Registro> registroList1) {
        this.registroList1 = registroList1;
    }

    @XmlTransient
    public List<Registro> getRegistroList2() {
        return registroList2;
    }

    public void setRegistroList2(List<Registro> registroList2) {
        this.registroList2 = registroList2;
    }
    
    @XmlTransient
    public List<SbPregunta> getSbPreguntaList() {
        return sbPreguntaList;
    }

    public void setSbPreguntaList(List<SbPregunta> sbPreguntaList) {
        this.sbPreguntaList = sbPreguntaList;
    }

    @XmlTransient
    public List<GamacSbPersona> getGamacSbPersonaList() {
        return gamacSbPersonaList;
    }

    public void setGamacSbPersonaList(List<GamacSbPersona> gamacSbPersonaList) {
        this.gamacSbPersonaList = gamacSbPersonaList;
    }

    @XmlTransient
    public List<GamacSbPersona> getGamacSbPersonaList1() {
        return gamacSbPersonaList1;
    }

    public void setGamacSbPersonaList1(List<GamacSbPersona> gamacSbPersonaList1) {
        this.gamacSbPersonaList1 = gamacSbPersonaList1;
    }

    @XmlTransient
    public List<GamacSbPersona> getGamacSbPersonaList2() {
        return gamacSbPersonaList2;
    }

    public void setGamacSbPersonaList2(List<GamacSbPersona> gamacSbPersonaList2) {
        this.gamacSbPersonaList2 = gamacSbPersonaList2;
    }

    @XmlTransient
    public List<GamacSbPersona> getGamacSbPersonaList3() {
        return gamacSbPersonaList3;
    }

    public void setGamacSbPersonaList3(List<GamacSbPersona> gamacSbPersonaList3) {
        this.gamacSbPersonaList3 = gamacSbPersonaList3;
    }

    @XmlTransient
    public List<GamacSbPersona> getGamacSbPersonaList4() {
        return gamacSbPersonaList4;
    }

    public void setGamacSbPersonaList4(List<GamacSbPersona> gamacSbPersonaList4) {
        this.gamacSbPersonaList4 = gamacSbPersonaList4;
    }

    @XmlTransient
    public List<GamacSbPersona> getGamacSbPersonaList5() {
        return gamacSbPersonaList5;
    }

    public void setGamacSbPersonaList5(List<GamacSbPersona> gamacSbPersonaList5) {
        this.gamacSbPersonaList5 = gamacSbPersonaList5;
    }    
}
