package by.ivanshilyaev.crawler.controller;

import by.ivanshilyaev.crawler.service.StatisticsService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Runner {
    private static final String SEARCH_ENGINE_ID = "009675484660055542115:3viexnmtvvq";
    private static final String API_KEY = "AIzaSyChq7hCghn5jL6Ydmg2V0IVEzmGMa9NhUo";
    private static String searchQuery;

    private static final int LINK_DEPTH = 1;
    private static final int MAX_VISITED_PAGES = 300;
    private static ConcurrentLinkedQueue<String> linkQueue = new ConcurrentLinkedQueue<>();

    private static final Logger LOGGER = LogManager.getLogger();

    private static String[] attrs = {"Spring", "Java", "and", "main"};

    public static void main(String[] args) {
        try {
            searchQuery = "java spring";

            // Instance Custom search
            Customsearch cs = new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), null)
                    .setApplicationName("webCrawler")
                    .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer(API_KEY))
                    .build();

            // Set search parameter
            Customsearch.Cse.List list = cs.cse().list(searchQuery).setCx(SEARCH_ENGINE_ID);

            // Execute search (first 10 pages)
            Search result = list.execute();
            if (result.getItems() != null) {
                for (Result resultItem : result.getItems()) {
                    String url = resultItem.getLink();
                    StatisticsService.addLinks(linkQueue, LINK_DEPTH, url);
                }
            }

            int visitedPages = 0;
            while (visitedPages < MAX_VISITED_PAGES) {
                System.out.println(visitedPages + ": " + StatisticsService.buildStatistics(linkQueue.poll(), attrs));
                ++visitedPages;
            }
        } catch (IOException | GeneralSecurityException e) {
            LOGGER.error("Search failure", e);
        }
    }
}
