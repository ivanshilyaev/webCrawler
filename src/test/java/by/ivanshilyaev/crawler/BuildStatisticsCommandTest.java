package by.ivanshilyaev.crawler;

import by.ivanshilyaev.crawler.controller.Controller;
import by.ivanshilyaev.crawler.service.BuildStatisticsCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BuildStatisticsCommandTest {

    @Test
    public void buildStatistics() {
        try {
            String url = "https://www.youtube.com/";
            String[] attrs = {"History", "YouTube"};
            Thread thread = new Thread(new BuildStatisticsCommand(url, attrs));
            thread.start();
            thread.join();
            assertTrue(Controller.queueWithLinksAndStatistics.size() > 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
