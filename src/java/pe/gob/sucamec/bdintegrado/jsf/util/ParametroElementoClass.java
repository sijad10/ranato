/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.jsf.util;

import java.math.BigDecimal;
import java.util.Date;
import pe.gob.sucamec.sistemabase.data.SbDireccion;

/**
 *
 * @author gchavez
 */
public class ParametroElementoClass {

    private Long id;
    private String s_valor1;
    private String s_valor2;
    private String s_valor3;
    private String s_valor4;
    private String s_valor5;
    private String s_valor6;
    private Double d_valor1;
    private Double d_valor2;
    private BigDecimal bd_valor1;
    private BigDecimal bd_valor2;
    private Long l_valor1;
    private Long l_valor2;
    private int int_valor1;
    private int int_valor2;
    private Boolean check;
    private Date fecha1;
    
    //direccion para el modulo de autorizaciones
    private SbDireccion objDireccion;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the s_valor1
     */
    public String getS_valor1() {
        return s_valor1;
    }

    /**
     * @param s_valor1 the s_valor1 to set
     */
    public void setS_valor1(String s_valor1) {
        this.s_valor1 = s_valor1;
    }

    /**
     * @return the l_valor1
     */
    public Long getL_valor1() {
        return l_valor1;
    }

    /**
     * @param l_valor1 the l_valor1 to set
     */
    public void setL_valor1(Long l_valor1) {
        this.l_valor1 = l_valor1;
    }

    /**
     * @return the check
     */
    public Boolean getCheck() {
        return check;
    }

    /**
     * @param check the check to set
     */
    public void setCheck(Boolean check) {
        this.check = check;
    }

    /**
     * @return the s_valor2
     */
    public String getS_valor2() {
        return s_valor2;
    }

    /**
     * @param s_valor2 the s_valor2 to set
     */
    public void setS_valor2(String s_valor2) {
        this.s_valor2 = s_valor2;
    }

    /**
     * @return the s_valor3
     */
    public String getS_valor3() {
        return s_valor3;
    }

    /**
     * @param s_valor3 the s_valor3 to set
     */
    public void setS_valor3(String s_valor3) {
        this.s_valor3 = s_valor3;
    }

    /**
     * @return the s_valor4
     */
    public String getS_valor4() {
        return s_valor4;
    }

    /**
     * @param s_valor4 the s_valor4 to set
     */
    public void setS_valor4(String s_valor4) {
        this.s_valor4 = s_valor4;
    }

    /**
     * @return the s_valor5
     */
    public String getS_valor5() {
        return s_valor5;
    }

    /**
     * @param s_valor5 the s_valor5 to set
     */
    public void setS_valor5(String s_valor5) {
        this.s_valor5 = s_valor5;
    }

    /**
     * @return the s_valor6
     */
    public String getS_valor6() {
        return s_valor6;
    }

    /**
     * @param s_valor6 the s_valor6 to set
     */
    public void setS_valor6(String s_valor6) {
        this.s_valor6 = s_valor6;
    }

    /**
     * @return the d_valor1
     */
    public Double getD_valor1() {
        return d_valor1;
    }

    /**
     * @param d_valor1 the d_valor1 to set
     */
    public void setD_valor1(Double d_valor1) {
        this.d_valor1 = d_valor1;
    }

    /**
     * @return the d_valor2
     */
    public Double getD_valor2() {
        return d_valor2;
    }

    /**
     * @param d_valor2 the d_valor2 to set
     */
    public void setD_valor2(Double d_valor2) {
        this.d_valor2 = d_valor2;
    }

    /**
     * @return the bd_valor1
     */
    public BigDecimal getBd_valor1() {
        return bd_valor1;
    }

    /**
     * @param bd_valor1 the bd_valor1 to set
     */
    public void setBd_valor1(BigDecimal bd_valor1) {
        this.bd_valor1 = bd_valor1;
    }

    /**
     * @return the bd_valor2
     */
    public BigDecimal getBd_valor2() {
        return bd_valor2;
    }

    /**
     * @param bd_valor2 the bd_valor2 to set
     */
    public void setBd_valor2(BigDecimal bd_valor2) {
        this.bd_valor2 = bd_valor2;
    }

    /**
     * @return the l_valor2
     */
    public Long getL_valor2() {
        return l_valor2;
    }

    /**
     * @param l_valor2 the l_valor2 to set
     */
    public void setL_valor2(Long l_valor2) {
        this.l_valor2 = l_valor2;
    }

    /**
     * @return the int_valor1
     */
    public int getInt_valor1() {
        return int_valor1;
    }

    /**
     * @param int_valor1 the int_valor1 to set
     */
    public void setInt_valor1(int int_valor1) {
        this.int_valor1 = int_valor1;
    }

    /**
     * @return the int_valor2
     */
    public int getInt_valor2() {
        return int_valor2;
    }

    /**
     * @param int_valor2 the int_valor2 to set
     */
    public void setInt_valor2(int int_valor2) {
        this.int_valor2 = int_valor2;
    }

    public Date getFecha1() {
        return fecha1;
    }

    public void setFecha1(Date fecha1) {
        this.fecha1 = fecha1;
    }

    public SbDireccion getObjDireccion() {
        return objDireccion;
    }

    public void setObjDireccion(SbDireccion objDireccion) {
        this.objDireccion = objDireccion;
    }

    /**
     * Converter, para selectManyMenus, relaciones muchos a muchos y otros
     * controllers de JSF
     */
//    @FacesConverter(value = "ParametroElementoClassConverter")
//    public static class ParametroElementoClassControllerConverterN extends ParametroElementoClassControllerConverter {
//    }
    /**
     * Converter, para selectOneMenus y otros controllers de JSF
     *
     * @param facesContext
     * @param component
     * @param object
     * @return
     */
//    @FacesConverter(forClass = ParametroElementoClass.class)
//    public static class ParametroElementoClassConverter implements Converter {
//        @Override
//        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
//            if (value == null || value.length() == 0) {
//                return null;
//            }
//            ParametroElementoClassController c = (ParametroElementoClassController) JsfUtil.obtenerBean("ParametroElementoClassController", ParametroElementoClassController.class);
//            return c.getParametroElementoClass(Long.valueOf(value));
//        }
//        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
//            if (object == null) {
//                return null;
//            }
//            if (object instanceof ParametroElementoClass) {
//                ParametroElementoClass o = (ParametroElementoClass) object;
//                return o.getId().toString();
//            } else {
//                throw new IllegalArgumentException("El objeto " + object + " es del tipo " + object.getClass().getName() + "; se esperaba: " + ParametroElementoClass.class.getName());
//            }
//        }
//    }
}
