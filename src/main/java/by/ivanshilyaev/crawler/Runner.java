package by.ivanshilyaev.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Runner {
    public static void main(String[] args) {
        try {
            String blogUrl = "https://spring.io/blog";
            Document document = Jsoup.connect(blogUrl).get();
            Elements elements = document.select("p");
            elements.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
