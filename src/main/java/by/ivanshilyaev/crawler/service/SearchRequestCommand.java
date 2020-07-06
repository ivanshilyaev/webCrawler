package by.ivanshilyaev.crawler.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.model.Search;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SearchRequestCommand {
    private static final String SEARCH_ENGINE_ID = "009675484660055542115:3viexnmtvvq";
    private static final String API_KEY = "AIzaSyChq7hCghn5jL6Ydmg2V0IVEzmGMa9NhUo";

    public Search getSearchResult(String searchQuery) throws IOException, GeneralSecurityException {
        // Instance Custom search
        Customsearch cs = new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), null)
                .setApplicationName("webCrawler")
                .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer(API_KEY))
                .build();
        // Set search parameter
        Customsearch.Cse.List list = cs.cse().list(searchQuery).setCx(SEARCH_ENGINE_ID);
        // Execute search (first 10 pages)
        return list.execute();
    }
}
