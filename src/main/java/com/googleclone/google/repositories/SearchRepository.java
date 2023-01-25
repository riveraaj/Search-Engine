package com.googleclone.google.repositories;

import com.googleclone.google.models.WebPages;

import java.util.List;

public interface SearchRepository {

    List<WebPages> getLinksToIndex();

    WebPages getByUrl(String url);

    List<WebPages> search(String textSearch);

    void save(WebPages oWebPages);

    boolean exist(String link);
}
