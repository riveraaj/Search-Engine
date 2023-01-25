package com.googleclone.google.services;

import com.googleclone.google.models.WebPages;
import com.googleclone.google.repositories.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private SearchRepository repository;
    public List<WebPages> search(String textSearch){
        return repository.search(textSearch);
    }
    public void save(WebPages oWebPages){
        repository.save(oWebPages);
    }

    public boolean exist(String link) {
        return repository.exist(link);
    }

    public List<WebPages> getLinksToIndex(){
        return repository.getLinksToIndex();
    }

}
