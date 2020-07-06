package by.ivanshilyaev.crawler.controller;

import by.ivanshilyaev.crawler.service.AddLinksCommand;
import by.ivanshilyaev.crawler.service.BuildStatisticsCommand;
import by.ivanshilyaev.crawler.service.SearchRequestCommand;
import by.ivanshilyaev.crawler.service.WriteStatisticsCommand;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;

public class Controller {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String[] attrs = {"Tesla", "Musk", "Gigafactory", "Elon Mask"};
    public static final int LINK_DEPTH = 1;
    private static final int MAX_VISITED_PAGES = 20;
    public static Queue<String> linkQueue = new ConcurrentLinkedQueue<>();
    public static Queue<String> queueWithLinksAndStatistics = new ConcurrentLinkedQueue<>();
    public static ConcurrentMap<Integer, String> mapWithLinksAndTotalHits = new ConcurrentSkipListMap<>(Collections.reverseOrder());
    private static String statisticsFileName = "/Users/ivansilaev/Downloads/gitRepos/webCrawler/src/main/resources/result.csv";
    private static String top10resultsFileName = "/Users/ivansilaev/Downloads/gitRepos/webCrawler/src/main/resources/top10.csv";

    private static void waitTillAllTasksCompleted(ExecutorService service) throws InterruptedException {
        ThreadPoolExecutor pool = (ThreadPoolExecutor) service;
        while (pool.getActiveCount() != 0) {
            TimeUnit.MILLISECONDS.sleep(500);
        }
        service.shutdown();
    }

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
            waitTillAllTasksCompleted(executorService);

            // 3. building results
            executorService = Executors.newCachedThreadPool();
            int visitedPages = 0;
            while (visitedPages < MAX_VISITED_PAGES) {
                executorService.submit(new BuildStatisticsCommand(linkQueue.poll(), attrs));
                ++visitedPages;
            }
            waitTillAllTasksCompleted(executorService);

            // 4. writing results to file
            // print result
            File file = new File(statisticsFileName);
            WriteStatisticsCommand writeStatisticsCommand = new WriteStatisticsCommand();
            writeStatisticsCommand.writeStatistics(file, queueWithLinksAndStatistics);

            // print top 10 pages
            file = new File(top10resultsFileName);
            try (PrintWriter writer = new PrintWriter(file)) {
                int count = 0;
                Iterator<Map.Entry<Integer, String>> iterator = mapWithLinksAndTotalHits.entrySet().iterator();
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
