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

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

public class Runner {
    private static final String SEARCH_ENGINE_ID = "009675484660055542115:3viexnmtvvq";
    private static final String API_KEY = "AIzaSyChq7hCghn5jL6Ydmg2V0IVEzmGMa9NhUo";
    private static String searchQuery;

    private static final int LINK_DEPTH = 1;
    private static final int MAX_VISITED_PAGES = 20;
    private static ConcurrentLinkedQueue<String> linkQueue = new ConcurrentLinkedQueue<>();
    private static ConcurrentSkipListMap<Integer, String> resultMap = new ConcurrentSkipListMap<>(Collections.reverseOrder());

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String[] attrs = {"Tesla", "Musk", "Gigafactory", "Elon Mask"};

    public static String convertToCSV(String[] data) {
        return String.join(",", data);
    }

    public static void main(String[] args) {
        try {
            searchQuery = Arrays.toString(attrs);

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
            StatisticsService statisticsService = StatisticsService.getInstance();
            if (result.getItems() != null) {
                for (Result resultItem : result.getItems()) {
                    String url = resultItem.getLink();
                    statisticsService.addLinks(linkQueue, LINK_DEPTH, url);
                }
            }

            // print result
            File file = new File("/Users/ivansilaev/Downloads/gitRepos/webCrawler/src/main/resources/result.csv");
            try (PrintWriter writer = new PrintWriter(file)) {
                int visitedPages = 0;
                while (visitedPages < MAX_VISITED_PAGES) {
                    writer.println(statisticsService.buildStatistics(linkQueue.poll(), attrs, resultMap));
                    ++visitedPages;
                }
            }

            // print top 10 pages
            file = new File("/Users/ivansilaev/Downloads/gitRepos/webCrawler/src/main/resources/top10.csv");
            try (PrintWriter writer = new PrintWriter(file)) {
                int count = 0;
                Iterator<Map.Entry<Integer, String>> iterator = resultMap.entrySet().iterator();
                while (count < 10) {
                    if (iterator.hasNext()) {
                        Map.Entry<Integer, String> pair = iterator.next();
                        writer.println(pair.getKey() + ", " + pair.getValue());
                    }
                    ++count;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Search failure", e);
        }
    }
}
