/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;

/**
 * Crea un facade básico con opciones para crear, editar, eliminar y encontrar datos.
 *
 * @author mespinoza
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;
    public final int MAX_RES = 500;

    /**
     * Crea un facade usando un entityClass
     *
     * @param entityClass el tipo de entidad (tabla) de esta clase.
     */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    /**
     * Crea un nuevo registro en la tabla
     * 
     * @param entity La entidad con los datos a ingresar en el registro.
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Edita un registro en la tabla
     * 
     * @param entity Datos de la entidad a editar. 
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Remueve un registro de la tabla, de preferencia usar borrado logico en lugar de esta funcion.
     * Tener cuidado que el ORM elimina los datos en cascada.
     * 
     * @param entity Datos con la entidad a eliminar.
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

/**
 * Encuentra una entidad en una tabla
 * 
 * @param id La llave de la entidad.
 * @return Devuelve la entidad encontrada, sino null.
 */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Busca todos los registros de una tabla
     * @return Una lista con todos los registros de la tabla.
     */
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Busca un rango de registros de una tabla. Permite hacer Datatables con paginadores en la tabla.
     * @param range Arreglo con el rango de datos [0]=rango inicial, [1]=rango final
     * @return Una lista con los registros encontrados.
     */
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * Devuelve la cantidad de registros de una tabla
     * @return La cantidad de registros de la tabla.
     */
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public void createValidator(T entity) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (constraintViolations.size() > 0) {
            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> cv = iterator.next();
                System.err.println(cv.getRootBeanClass().getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());

                JsfUtil.mensajeError(cv.getRootBeanClass().getSimpleName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
            }
        } else {
            getEntityManager().persist(entity);
        }
    }
    
    public void editValidator(T entity) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (constraintViolations.size() > 0) {
            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> cv = iterator.next();
                System.err.println(cv.getRootBeanClass().getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());

                //JsfUtil.mensajeError(cv.getRootBeanClass().getSimpleName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
            }
        } else {
            getEntityManager().merge(entity);
        }
    }
}
