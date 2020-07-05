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
import java.util.concurrent.ConcurrentSkipListMap;

public class StatisticsService {
    private static final StatisticsService INSTANCE = new StatisticsService();

    public static StatisticsService getInstance() {
        return INSTANCE;
    }

    private StatisticsService() {
    }

    public void addLinks(Queue<String> linkQueue, int depth, String url) throws IOException {
        Document root = Jsoup.connect(url).get();
        Set<Element> tempSet;
        Set<Element> set = new HashSet<>();
        set.add(root);
        for (int i = 0; i < depth; ++i) {
            tempSet = set;
            set = new HashSet<>();
            for (Element element : tempSet) {
                set.add(element);
                // select all links on the page
                Elements links = element.select("a[href]");
                set.addAll(links);
            }
        }
        for (Element link : set) {
            linkQueue.add(link.attr("abs:href"));
        }
    }

    public String buildStatistics(String url, String[] attrs, ConcurrentSkipListMap<Integer, String> resultMap) {
        try {
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
            builder.append(totalHits);
            resultMap.put(totalHits, url);
            return builder.toString();
        } catch (IOException | IllegalArgumentException e) {
            return "Invalid link";
        }
    }
}
