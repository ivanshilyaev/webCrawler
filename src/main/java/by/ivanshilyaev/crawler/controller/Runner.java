package by.ivanshilyaev.crawler.controller;

import by.ivanshilyaev.crawler.service.AddLinksCommand;
import by.ivanshilyaev.crawler.service.BuildStatisticsCommand;
import by.ivanshilyaev.crawler.service.SearchRequestCommand;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;

public class Runner {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String[] attrs = {"Tesla", "Musk", "Gigafactory", "Elon Mask"};

    public static final int LINK_DEPTH = 1;
    private static final int MAX_VISITED_PAGES = 20;

    public static Queue<String> linkQueue = new ConcurrentLinkedQueue<>();

    public static Queue<String> resultQueue = new ConcurrentLinkedQueue<>();

    public static ConcurrentMap<Integer, String> resultMap = new ConcurrentSkipListMap<>(Collections.reverseOrder());

    public static void main(String[] args) {
        try {
            // 1. search
            String searchQuery = Arrays.toString(attrs);
            SearchRequestCommand searchRequestCommand = new SearchRequestCommand();
            Search result = searchRequestCommand.getSearchResult(searchQuery);

            // 2. adding links
            ExecutorService executorService = Executors.newCachedThreadPool();
            if (result.getItems() != null) {
                for (Result resultItem : result.getItems()) {
                    String url = resultItem.getLink();
                    executorService.submit(new AddLinksCommand(url, LINK_DEPTH));
                }
            }
            ThreadPoolExecutor pool = (ThreadPoolExecutor) executorService;
            while (pool.getActiveCount() != 0) {
                TimeUnit.MILLISECONDS.sleep(500);
            }
            executorService.shutdown();

            // 3. building results
            executorService = Executors.newCachedThreadPool();
            int visitedPages = 0;
            while (visitedPages < MAX_VISITED_PAGES) {
                executorService.submit(new BuildStatisticsCommand(linkQueue.poll(), attrs));
                ++visitedPages;
            }
            pool = (ThreadPoolExecutor) executorService;
            while (pool.getActiveCount() != 0) {
                TimeUnit.MILLISECONDS.sleep(500);
            }
            executorService.shutdown();

            // 4. writing results to file
            // print result
            File file = new File("/Users/ivansilaev/Downloads/gitRepos/webCrawler/src/main/resources/result.csv");
            try (PrintWriter writer = new PrintWriter(file)) {
                while (!resultQueue.isEmpty()) {
                    writer.println(resultQueue.poll());
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
                        System.out.println(pair.getKey() + ", " + pair.getValue());
                    }
                    ++count;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Search failure", e);
        }
    }
}
