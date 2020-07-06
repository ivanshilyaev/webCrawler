package by.ivanshilyaev.crawler.service;

import by.ivanshilyaev.crawler.controller.Runner;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class BuildStatisticsCommand implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger();

    private String url;
    private String[] attrs;

    public BuildStatisticsCommand(String url, String[] attrs) {
        this.url = url;
        this.attrs = attrs;
    }

    @Override
    public void run() {
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
            Runner.resultMap.put(totalHits, url);
            Runner.resultQueue.add(builder.toString());
        } catch (IOException | IllegalArgumentException e) {
            Runner.resultQueue.add("Invalid link");
        }
    }
}
