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

public class AddLinksCommand implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger();

    private String url;
    private int depth;

    public AddLinksCommand(String url, int depth) {
        this.url = url;
        this.depth = depth;
    }

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
