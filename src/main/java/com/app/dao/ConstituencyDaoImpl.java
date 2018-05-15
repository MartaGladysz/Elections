package com.app.dao;

import com.app.dao.generic.AbstractGenericDaoImpl;
import com.app.model.Constituency;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


@Repository
public class ConstituencyDaoImpl extends AbstractGenericDaoImpl<Constituency> implements ConstituencyDao {

    @SuppressWarnings("all")
    @Override
    public List<Constituency> findAll() {
        EntityManager entityManager = getEntityManager();

        List<Constituency> elements = null;
        if (entityManager != null) {
            Query query = entityManager.createQuery("SELECT c from Constituency c");
            elements = query.getResultList();

            for (int i = 0; i < elements.size(); i++) {
                elements.get(i).getVoters().size();
                elements.get(i).getCandidates().size();
            }
        }
        return elements;
    }
}




