package by.ivanshilyaev.crawler.controller;

import by.ivanshilyaev.crawler.service.*;
import by.ivanshilyaev.crawler.service.exception.ServiceException;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.*;

public class Controller {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int LINK_DEPTH = 1;
    private static final int MAX_VISITED_PAGES = 100;
    public static Queue<String> linkQueue = new ConcurrentLinkedQueue<>();
    public static Queue<String> queueWithLinksAndStatistics = new ConcurrentLinkedQueue<>();
    public static ConcurrentMap<Integer, String> mapWithLinksAndTotalHits = new ConcurrentSkipListMap<>(Collections.reverseOrder());
    private static final String STATISTICS_FILENAME = "src/main/resources/result.csv";
    private static final String TOP_10_RESULTS_FILENAME = "src/main/resources/top10.csv";

    private static void waitTillAllTasksCompleted(ExecutorService service) {
        ThreadPoolExecutor pool = (ThreadPoolExecutor) service;
        while (pool.getActiveCount() != 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                LOGGER.error("Error while working with threads", e);
            }
        }
        service.shutdown();
    }

    public static void main(String[] args) {
        try {
            // 1. search
            String searchQuery = Arrays.toString(args);
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
                executorService.submit(new BuildStatisticsCommand(linkQueue.poll(), args));
                ++visitedPages;
            }
            waitTillAllTasksCompleted(executorService);

            // 4. writing results to file
            // print results
            File file = new File(STATISTICS_FILENAME);
            WriteStatisticsCommand writeStatisticsCommand = new WriteStatisticsCommand();
            writeStatisticsCommand.writeStatistics(file, queueWithLinksAndStatistics);

            // print top 10 pages
            file = new File(TOP_10_RESULTS_FILENAME);
            WriteTop10ResultsCommand writeTop10ResultsCommand = new WriteTop10ResultsCommand();
            writeTop10ResultsCommand.writeResults(file, mapWithLinksAndTotalHits);
        } catch (GeneralSecurityException e) {
            LOGGER.error("Search error", e);
        } catch (ServiceException e) {
            LOGGER.error("Service error", e);
        } catch (IOException e) {
            LOGGER.error("Unable to work with files", e);
        }
    }
}
