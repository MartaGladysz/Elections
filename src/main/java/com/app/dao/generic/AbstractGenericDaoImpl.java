package com.app.dao.generic;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractGenericDaoImpl<T> implements GenericDao<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> entityClass = (Class<T>)((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0];

    protected EntityManager getEntityManager(){
        return entityManager;
    }

    @Override
    public void add(T t){
        if(entityManager != null && t != null){
            entityManager.persist(t);
        }
    }

    @Override
    public void update(T t){
        if(entityManager != null && t != null){
            entityManager.merge(t);
        }
    }

    @Override
    public void delete(Long id){
        if(entityManager != null && id != null){
            T t = entityManager.find(entityClass, id);
            entityManager.remove(t);
        }
    }

    @Override
    public Optional<T> findOneById(Long id) {
        Optional<T> element = Optional.empty();
        if(entityManager != null && id != null){
            element = Optional.of((T) entityManager.find(entityClass, id));
        }

        return element;
    }

    @Override
    public List<T> findAll() {
        List<T> elements = null;
        if(entityManager != null){
            Query query = entityManager.createQuery("SELECT t from "+entityClass.getCanonicalName()+" t");
            elements = query.getResultList();
        }
        return elements;
    }
}

