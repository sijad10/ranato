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
import javax.persistence.JoinTable;
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
import pe.gob.sucamec.sistemabase.data.SbDireccion;

/**
 *
 * @author mespinoza
 */
@Entity
@Table(name = "TIPO_BASE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoBaseGt.findAll", query = "SELECT t FROM TipoBaseGt t"),
    @NamedQuery(name = "TipoBaseGt.findById", query = "SELECT t FROM TipoBaseGt t WHERE t.id = :id"),
    @NamedQuery(name = "TipoBaseGt.findByNombre", query = "SELECT t FROM TipoBaseGt t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoBaseGt.findByAbreviatura", query = "SELECT t FROM TipoBaseGt t WHERE t.abreviatura = :abreviatura"),
    @NamedQuery(name = "TipoBaseGt.findByDescripcion", query = "SELECT t FROM TipoBaseGt t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TipoBaseGt.findByCodProg", query = "SELECT t FROM TipoBaseGt t WHERE t.codProg = :codProg"),
    @NamedQuery(name = "TipoBaseGt.findByOrden", query = "SELECT t FROM TipoBaseGt t WHERE t.orden = :orden"),
    @NamedQuery(name = "TipoBaseGt.findByActivo", query = "SELECT t FROM TipoBaseGt t WHERE t.activo = :activo"),
    @NamedQuery(name = "TipoBaseGt.findByAudLogin", query = "SELECT t FROM TipoBaseGt t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TipoBaseGt.findByAudNumIp", query = "SELECT t FROM TipoBaseGt t WHERE t.audNumIp = :audNumIp")})
public class TipoBaseGt implements Serializable {

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
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoObservacionId")
    private List<EppObservacion> eppObservacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProcesoId")
    private List<EppObservacion> eppObservacionList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAutenId")
    private List<SbUsuarioGt> sbUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbUsuarioGt> sbUsuarioList1;
    @OneToMany(mappedBy = "areaId")
    private List<SbUsuarioGt> sbUsuarioList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "viaId")
    private List<SbDireccionGt> sbDireccionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbDireccionGt> sbDireccionList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zonaId")
    private List<SbDireccionGt> sbDireccionList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbLogUsuarioGt> sbLogUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbValidacionWebGt> sbValidacionWebList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "viaId")
    private List<EppPuertoAduanero> eppPuertoAduaneroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sedeOrganizaId")
    private List<EppCapacitacion> eppCapacitacionList;
    @OneToMany(mappedBy = "tipoId")
    private List<TipoBaseGt> tipoBaseList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProcId")
    private List<EppLocal> eppLocalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private List<SbNumeracion> sbNumeracionList;
    @OneToMany(mappedBy = "generoId")
    private List<SbPersonaGt> sbPersonaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbPersonaGt> sbPersonaList1;
    @OneToMany(mappedBy = "instituLabId")
    private List<SbPersonaGt> sbPersonaList2;
    @OneToMany(mappedBy = "tipoDoc")
    private List<SbPersonaGt> sbPersonaList3;
    @OneToMany(mappedBy = "estCivilId")
    private List<SbPersonaGt> sbPersonaList4;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbRelacionPersonaGt> sbRelacionPersonaList;
    @OneToMany(mappedBy = "areaId")
    private List<EppDocumento> eppDocumentoList;
    @OneToMany(mappedBy = "destAreaId")
    private List<EppDocumento> eppDocumentoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRegId")
    private List<EppRegistro> eppRegistroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoOpeId")
    private List<EppRegistro> eppRegistroList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProId")
    private List<EppRegistro> eppRegistroList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mesId")
    private List<EppLibroMes> eppLibroMesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<EppLibroAdjunto> eppLibroAdjuntoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPrioridad")
    private List<SbMedioContactoGt> sbMedioContactoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<SbMedioContactoGt> sbMedioContactoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoId")
    private List<EppDocumentoAdjunto> eppDocumentoAdjuntoList;
    @JoinTable(schema = "BDINTEGRADO", name = "AMA_CENTRO_INPE_DIRECCION", joinColumns = {
        @JoinColumn(name = "CENTRO_INPE_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "DIRECCION_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbDireccionGt> sbDireccionInpeList;
    @JoinTable(schema = "BDINTEGRADO", name = "SB_TUPA_REQUISITO_FORMATO", joinColumns = {
        @JoinColumn(name = "FORMATO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "TUPA_REQUISITO_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbTupaRequisito> sbTupaRequisitoList;
    
    @OneToMany(mappedBy = "tipoDoc")
    private List<SbEmpresaExtranjera> sbEmpresaExtranjeraList;

    public List<SbTupaRequisito> getSbTupaRequisitoList() {
        return sbTupaRequisitoList;
    }

    public void setSbTupaRequisitoList(List<SbTupaRequisito> sbTupaRequisitoList) {
        this.sbTupaRequisitoList = sbTupaRequisitoList;
    }

    public TipoBaseGt() {
    }

    public TipoBaseGt(Long id) {
        this.id = id;
    }

    public TipoBaseGt(Long id, String nombre, String codProg, short activo, String audLogin, String audNumIp) {
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
    public List<EppObservacion> getEppObservacionList() {
        return eppObservacionList;
    }

    public void setEppObservacionList(List<EppObservacion> eppObservacionList) {
        this.eppObservacionList = eppObservacionList;
    }

    @XmlTransient
    public List<EppObservacion> getEppObservacionList1() {
        return eppObservacionList1;
    }

    public void setEppObservacionList1(List<EppObservacion> eppObservacionList1) {
        this.eppObservacionList1 = eppObservacionList1;
    }

    @XmlTransient
    public List<SbUsuarioGt> getSbUsuarioList() {
        return sbUsuarioList;
    }

    public void setSbUsuarioList(List<SbUsuarioGt> sbUsuarioList) {
        this.sbUsuarioList = sbUsuarioList;
    }

    @XmlTransient
    public List<SbUsuarioGt> getSbUsuarioList1() {
        return sbUsuarioList1;
    }

    public void setSbUsuarioList1(List<SbUsuarioGt> sbUsuarioList1) {
        this.sbUsuarioList1 = sbUsuarioList1;
    }

    @XmlTransient
    public List<SbUsuarioGt> getSbUsuarioList2() {
        return sbUsuarioList2;
    }

    public void setSbUsuarioList2(List<SbUsuarioGt> sbUsuarioList2) {
        this.sbUsuarioList2 = sbUsuarioList2;
    }

    @XmlTransient
    public List<SbDireccionGt> getSbDireccionList() {
        return sbDireccionList;
    }

    public void setSbDireccionList(List<SbDireccionGt> sbDireccionList) {
        this.sbDireccionList = sbDireccionList;
    }

    @XmlTransient
    public List<SbDireccionGt> getSbDireccionList1() {
        return sbDireccionList1;
    }

    public void setSbDireccionList1(List<SbDireccionGt> sbDireccionList1) {
        this.sbDireccionList1 = sbDireccionList1;
    }

    @XmlTransient
    public List<SbDireccionGt> getSbDireccionList2() {
        return sbDireccionList2;
    }

    public void setSbDireccionList2(List<SbDireccionGt> sbDireccionList2) {
        this.sbDireccionList2 = sbDireccionList2;
    }

    @XmlTransient
    public List<SbLogUsuarioGt> getSbLogUsuarioList() {
        return sbLogUsuarioList;
    }

    public void setSbLogUsuarioList(List<SbLogUsuarioGt> sbLogUsuarioList) {
        this.sbLogUsuarioList = sbLogUsuarioList;
    }

    @XmlTransient
    public List<SbValidacionWebGt> getSbValidacionWebList() {
        return sbValidacionWebList;
    }

    public void setSbValidacionWebList(List<SbValidacionWebGt> sbValidacionWebList) {
        this.sbValidacionWebList = sbValidacionWebList;
    }

    @XmlTransient
    public List<EppPuertoAduanero> getEppPuertoAduaneroList() {
        return eppPuertoAduaneroList;
    }

    public void setEppPuertoAduaneroList(List<EppPuertoAduanero> eppPuertoAduaneroList) {
        this.eppPuertoAduaneroList = eppPuertoAduaneroList;
    }

    @XmlTransient
    public List<EppCapacitacion> getEppCapacitacionList() {
        return eppCapacitacionList;
    }

    public void setEppCapacitacionList(List<EppCapacitacion> eppCapacitacionList) {
        this.eppCapacitacionList = eppCapacitacionList;
    }

    @XmlTransient
    public List<TipoBaseGt> getTipoBaseList() {
        return tipoBaseList;
    }

    public void setTipoBaseList(List<TipoBaseGt> tipoBaseList) {
        this.tipoBaseList = tipoBaseList;
    }

    public TipoBaseGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBaseGt tipoId) {
        this.tipoId = tipoId;
    }

    @XmlTransient
    public List<EppLocal> getEppLocalList() {
        return eppLocalList;
    }

    public void setEppLocalList(List<EppLocal> eppLocalList) {
        this.eppLocalList = eppLocalList;
    }

    @XmlTransient
    public List<SbNumeracion> getSbNumeracionList() {
        return sbNumeracionList;
    }

    public void setSbNumeracionList(List<SbNumeracion> sbNumeracionList) {
        this.sbNumeracionList = sbNumeracionList;
    }

    @XmlTransient
    public List<SbPersonaGt> getSbPersonaList() {
        return sbPersonaList;
    }

    public void setSbPersonaList(List<SbPersonaGt> sbPersonaList) {
        this.sbPersonaList = sbPersonaList;
    }

    @XmlTransient
    public List<SbPersonaGt> getSbPersonaList1() {
        return sbPersonaList1;
    }

    public void setSbPersonaList1(List<SbPersonaGt> sbPersonaList1) {
        this.sbPersonaList1 = sbPersonaList1;
    }

    @XmlTransient
    public List<SbPersonaGt> getSbPersonaList2() {
        return sbPersonaList2;
    }

    public void setSbPersonaList2(List<SbPersonaGt> sbPersonaList2) {
        this.sbPersonaList2 = sbPersonaList2;
    }

    @XmlTransient
    public List<SbPersonaGt> getSbPersonaList3() {
        return sbPersonaList3;
    }

    public void setSbPersonaList3(List<SbPersonaGt> sbPersonaList3) {
        this.sbPersonaList3 = sbPersonaList3;
    }

    @XmlTransient
    public List<SbPersonaGt> getSbPersonaList4() {
        return sbPersonaList4;
    }

    public void setSbPersonaList4(List<SbPersonaGt> sbPersonaList4) {
        this.sbPersonaList4 = sbPersonaList4;
    }

    @XmlTransient
    public List<SbRelacionPersonaGt> getSbRelacionPersonaList() {
        return sbRelacionPersonaList;
    }

    public void setSbRelacionPersonaList(List<SbRelacionPersonaGt> sbRelacionPersonaList) {
        this.sbRelacionPersonaList = sbRelacionPersonaList;
    }

    @XmlTransient
    public List<EppDocumento> getEppDocumentoList() {
        return eppDocumentoList;
    }

    public void setEppDocumentoList(List<EppDocumento> eppDocumentoList) {
        this.eppDocumentoList = eppDocumentoList;
    }

    @XmlTransient
    public List<EppDocumento> getEppDocumentoList1() {
        return eppDocumentoList1;
    }

    public void setEppDocumentoList1(List<EppDocumento> eppDocumentoList1) {
        this.eppDocumentoList1 = eppDocumentoList1;
    }

    @XmlTransient
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
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
    public List<EppLibroAdjunto> getEppLibroAdjuntoList() {
        return eppLibroAdjuntoList;
    }

    public void setEppLibroAdjuntoList(List<EppLibroAdjunto> eppLibroAdjuntoList) {
        this.eppLibroAdjuntoList = eppLibroAdjuntoList;
    }

    @XmlTransient
    public List<SbMedioContactoGt> getSbMedioContactoList() {
        return sbMedioContactoList;
    }

    public void setSbMedioContactoList(List<SbMedioContactoGt> sbMedioContactoList) {
        this.sbMedioContactoList = sbMedioContactoList;
    }

    @XmlTransient
    public List<SbMedioContactoGt> getSbMedioContactoList1() {
        return sbMedioContactoList1;
    }

    public void setSbMedioContactoList1(List<SbMedioContactoGt> sbMedioContactoList1) {
        this.sbMedioContactoList1 = sbMedioContactoList1;
    }

    @XmlTransient
    public List<EppDocumentoAdjunto> getEppDocumentoAdjuntoList() {
        return eppDocumentoAdjuntoList;
    }

    public void setEppDocumentoAdjuntoList(List<EppDocumentoAdjunto> eppDocumentoAdjuntoList) {
        this.eppDocumentoAdjuntoList = eppDocumentoAdjuntoList;
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
        if (!(object instanceof TipoBaseGt)) {
            return false;
        }
        TipoBaseGt other = (TipoBaseGt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.TipoBase[ id=" + id + " ]";
    }

    @XmlTransient
    public List<SbDireccionGt> getSbDireccionInpeList() {
        return sbDireccionInpeList;
    }

    public void setSbDireccionInpeList(List<SbDireccionGt> sbDireccionInpeList) {
        this.sbDireccionInpeList = sbDireccionInpeList;
    }
    
    @XmlTransient
    public List<SbEmpresaExtranjera> getSbEmpresaExtranjeraList() {
        return sbEmpresaExtranjeraList;
    }

    public void setSbEmpresaExtranjeraList(List<SbEmpresaExtranjera> sbEmpresaExtranjeraList) {
        this.sbEmpresaExtranjeraList = sbEmpresaExtranjeraList;
    } 
}
