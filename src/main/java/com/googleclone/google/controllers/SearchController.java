package com.googleclone.google.controllers;

import com.googleclone.google.models.WebPages;
import com.googleclone.google.services.SearchService;
import com.googleclone.google.services.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SearchController {

    @Autowired
    private SearchService service;
    @Autowired
    private SpiderService spiderService;

    @CrossOrigin("*")
    @RequestMapping(value = "api/search", method = RequestMethod.GET)
    public List<WebPages> search(@RequestParam Map<String, String> params){
        String query = params.get("query");
        return service.search(query);
    }

    @RequestMapping(value = "api/test", method = RequestMethod.GET)
    public void search(){
        spiderService.indexWebPages();
    }

}
