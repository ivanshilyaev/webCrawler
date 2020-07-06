package by.ivanshilyaev.crawler;

import by.ivanshilyaev.crawler.controller.Controller;
import by.ivanshilyaev.crawler.service.AddLinksCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddLinksCommandTest {

    @Test
    public void addLinks() {
        try {
            String url = "https://www.youtube.com/";
            int linkDepth = 1;
            Thread thread = new Thread(new AddLinksCommand(url, linkDepth));
            thread.start();
            thread.join();
            assertTrue(Controller.linkQueue.size() > 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
