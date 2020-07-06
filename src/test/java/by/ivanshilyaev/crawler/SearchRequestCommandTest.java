package by.ivanshilyaev.crawler;

import by.ivanshilyaev.crawler.service.SearchRequestCommand;
import com.google.api.services.customsearch.model.Search;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchRequestCommandTest {

    @Test
    public void getSearchResults() {
        try {
            SearchRequestCommand command = new SearchRequestCommand();
            Search search = command.getSearchResult("youtube");
            assertTrue(search.getItems().size() > 0);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}
