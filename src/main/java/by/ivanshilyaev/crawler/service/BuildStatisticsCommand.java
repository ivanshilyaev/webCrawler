package by.ivanshilyaev.crawler.service;

import by.ivanshilyaev.crawler.controller.Controller;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Special class for creating statistics.
 *
 * @version 1.0
 * @since 2020-07-06
 */

public class BuildStatisticsCommand implements Runnable {
    /**
     * Concrete url we are working with.
     */
    private final String url;
    /**
     * Search attributes.
     */
    private final String[] attrs;

    /**
     * Creates {@code BuildStatisticsCommand} instance.
     *
     * @param url   concrete url.
     * @param attrs search attributes.
     */
    public BuildStatisticsCommand(String url, String[] attrs) {
        this.url = url;
        this.attrs = attrs;
    }

    /**
     * Overridden method of the {@code Runnable} interface.
     * Searches {@code attrs} on the given page and builds statistics in a special form.
     */
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
            Controller.mapWithLinksAndTotalHits.put(totalHits, url);
            Controller.queueWithLinksAndStatistics.add(builder.toString());
        } catch (IOException | IllegalArgumentException e) {
            Controller.queueWithLinksAndStatistics.add("Invalid link");
        }
    }
}
