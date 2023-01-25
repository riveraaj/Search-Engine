package com.googleclone.google.repositories;

import com.googleclone.google.models.WebPages;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class SearchRepositoryImp implements SearchRepository{

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @Override
    public List search(String textSearch) {
        String query = "FROM WebPages WHERE description like :textSearch";
        return entityManager.createQuery(query)
                .setParameter("textSearch", "%" + textSearch + "%")
                .getResultList();
    }

    @Transactional
    @Override
    public void save(WebPages oWebPages) {
        entityManager.merge(oWebPages);
    }


    @Override
    public List<WebPages> getLinksToIndex() {
        String query = "FROM WebPages WHERE tittle is null AND description is null";
        return entityManager.createQuery(query)
                .setMaxResults(100)
                .getResultList();
    }

    public WebPages getByUrl(String link) {
        String query = "FROM WebPage WHERE ur = :url";
        List<WebPages> list = entityManager.createQuery(query)
                .setParameter("url", link)
                .getResultList();
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public boolean exist(String link){
        return getByUrl(link) != null;
    }

}
