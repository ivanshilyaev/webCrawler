package by.ivanshilyaev.crawler;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Runner {
    private static final String SEARCH_ENGINE_ID = "009675484660055542115:3viexnmtvvq";
    private static final String API_KEY = "AIzaSyChq7hCghn5jL6Ydmg2V0IVEzmGMa9NhUo";
    private static String searchQuery;

    private static final Logger LOGGER = LogManager.getLogger();

    private static String[] attrs = {"Spring", "Boot", "Java", "and"};

    public static void main(String[] args) {
        try {
            searchQuery = "java spring boot";

            // Instance Custom search
            Customsearch cs = new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), null)
                    .setApplicationName("webCrawler")
                    .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer(API_KEY))
                    .build();

            //Set search parameter
            Customsearch.Cse.List list = cs.cse().list(searchQuery).setCx(SEARCH_ENGINE_ID);

            //Execute search
            Search result = list.execute();
            if (result.getItems() != null) {
                for (Result resultItem : result.getItems()) {
                    System.out.println(resultItem.getTitle() + ": " + resultItem.getLink());
                    System.out.println("Result:");
                    String url = resultItem.getLink();
                    Document doc = Jsoup.connect(url).get();
                    String text = doc.body().text();
                    for (String attr : attrs) {
                        System.out.println(attr + ": " + StringUtils.countMatches(text, attr));
                    }
                    System.out.println("\n");
                }
            }
        } catch (IOException | GeneralSecurityException e) {
            LOGGER.error("Search failure");
        }
    }
}
