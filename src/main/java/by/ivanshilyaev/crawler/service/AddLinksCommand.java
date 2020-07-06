package by.ivanshilyaev.crawler.service;

import by.ivanshilyaev.crawler.controller.Controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Special class that works with concrete url.
 *
 * @version 1.0
 * @since 2020-07-06
 */

public class AddLinksCommand implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Concrete url we are working with.
     */
    private final String url;
    /**
     * Depth of the links we are going to process.
     */
    private final int depth;

    /**
     * Creates {@code AddLinksCommand} instance.
     *
     * @param url   concrete url.
     * @param depth predefined link depth.
     */
    public AddLinksCommand(String url, int depth) {
        this.url = url;
        this.depth = depth;
    }

    /**
     * Overridden method of the {@code Runnable} interface.
     * Get all the links from the page with given depths and add them to the queue.
     */
    @Override
    public void run() {
        try {
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
                Controller.linkQueue.add(link.attr("abs:href"));
            }
        } catch (IOException e) {
            LOGGER.error("Couldn't get link", e);
        }
    }
}
