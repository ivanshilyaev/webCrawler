package by.ivanshilyaev.crawler.service;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class StatisticsService {
    private StatisticsService() {
    }

    public static void addLinks(Queue<String> linkQueue, int depth, String url) throws IOException {
        Document root = Jsoup.connect(url).get();
        Set<Element> tempSet;
        Set<Element> set = new HashSet<>();
        set.add(root);
        for (int i = 0; i < depth; ++i) {
            tempSet = set;
            set = new HashSet<>();
            for (Element element : tempSet) {
                set.add(element);
                Elements links = element.select("a[href]");
                set.addAll(links);
            }
        }
        for (Element link : set) {
            linkQueue.add(link.attr("abs:href"));
        }
    }

    public static String buildStatistics(String url, String[] attrs) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String text = doc.body().text();
        StringBuilder builder = new StringBuilder();
        builder.append(url).append(" ");
        int totalHits = 0;
        int currentHits;
        for (String attr : attrs) {
            currentHits = StringUtils.countMatches(text, attr);
            builder.append(currentHits).append(" ");
            totalHits += currentHits;
        }
        builder.append(totalHits).append("\n");
        return builder.toString();
    }
}
