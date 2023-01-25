package com.googleclone.google.services;

import com.googleclone.google.models.WebPages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
public class SpiderService {

    @Autowired
    private SearchService searchService;

    public void indexWebPages(){
        List<WebPages> linksToIndex = searchService.getLinksToIndex();
        linksToIndex.stream().parallel().forEach(webpage -> {
            try {
                indexWebPage(webpage);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        });
    }

    private void indexWebPage(WebPages webpage) throws Exception{
        String url = webpage.getUrl();
        String content = getWebContent(url);
        if (isBlank(content)) {
            return;
        }
        indexAndSaveWebPage(webpage, content);
        saveLinks(getDomain(url), content);
    }

    private String getDomain(String url) {
        String [] aux = url.split("/");
        return aux[0] + "//" + aux[2];
    }

    private void saveLinks(String domain, String content){
        List<String> links = getLinks(domain, content);
        links.stream().filter(link -> !searchService.exist(link))
                .map(link -> new WebPages(link))
                .forEach(webPage -> searchService.save(webPage));
    }

    public List<String> getLinks(String domain, String content){
        List<String> links = new ArrayList<>();

        String [] splitHref =  content.split("href=\"");
        List<String> listHref = new ArrayList<>(List.of(splitHref));
        //listHref.remove(0);
        listHref.forEach(strHref -> {
            String []  aux = strHref.split("\"");
            links.add(aux[0]);
        });
        return cleanLinsk(domain, links);
    }

    private List<String> cleanLinsk(String domain, List<String> links){
        String [] excludedExtensions = new String[]{
                "css",  "js",  "css",  "json",  "png", "woff2"
        };
        List<String> resultLinks = links.stream()
                .filter(link -> Arrays.stream(excludedExtensions).noneMatch(link::endsWith))
                .map(link -> link.startsWith("/") ? domain + link : link)
                .collect(Collectors.toList());

        List<String> uniqueLinks = new ArrayList<>();
        uniqueLinks.addAll(new HashSet<>(resultLinks));

        return uniqueLinks;
    }


    private void indexAndSaveWebPage(WebPages webPages, String content) {
        String tittle = getTittle(content);
        String description = getDescription(content);

        webPages.setDescription(description);
        webPages.setTittle(tittle);
        searchService.save(webPages);
    }

    public String getTittle(String content){
        String [] aux =  content.split("<title>");
        String [] aux2 = aux[1].split("</title>");
        return aux2[0];
    }

    public String getDescription(String content){
        String [] aux =  content.split("<meta name=\"description\" content=\"");
        String [] aux2 = aux[1].split("\">");
        return aux2[0];
    }

    private String getWebContent(String link){
        try{
            URL url = new URL(link);
            HttpsURLConnection conection = (HttpsURLConnection) url.openConnection();
            String encondin = conection.getContentEncoding();
            InputStream input = conection.getInputStream();
            Stream<String> lines = new BufferedReader(new InputStreamReader(input)).lines();
            return lines.collect(Collectors.joining());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "";
    }
}
