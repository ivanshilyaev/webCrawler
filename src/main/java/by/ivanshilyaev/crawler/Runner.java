package by.ivanshilyaev.crawler;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

public class Runner {
    private static String[] attrs = {"Spring", "Boot", "Java", "and"};

    public static void main(String[] args) {
        try {
            String blogUrl = "https://spring.io/blog";
            Document doc = Jsoup.connect(blogUrl).get();
            String text = doc.body().text();
            for (String attr : attrs) {
                System.out.println(attr + ": " + StringUtils.countMatches(text, attr));
            }

            String searchQuery = "instagram"; // The query to search
            String cx = "009675484660055542115:3viexnmtvvq"; // Search engine id

            // Instance Custom search
            Customsearch cs = new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), null)
                    .setApplicationName("MyApplication")
                    .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer("AIzaSyChq7hCghn5jL6Ydmg2V0IVEzmGMa9NhUo")) // API key
                    .build();

            //Set search parameter
            Customsearch.Cse.List list = cs.cse().list(searchQuery).setCx(cx);

            //Execute search
            Search result = list.execute();
            if (result.getItems() != null) {
                for (Result ri : result.getItems()) {
                    // Get title, link, body etc. from search
                    System.out.println(ri.getTitle() + ", " + ri.getLink());
                }
            }
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
