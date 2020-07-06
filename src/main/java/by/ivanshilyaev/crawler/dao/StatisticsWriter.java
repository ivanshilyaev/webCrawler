package by.ivanshilyaev.crawler.dao;

import by.ivanshilyaev.crawler.dao.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Queue;

public class StatisticsWriter {
    private static final Logger LOGGER = LogManager.getLogger();

    public void writeStatistics(File file, Queue<String> queue) throws DAOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            while (!queue.isEmpty()) {
                writer.println(queue.poll());
            }
        } catch (FileNotFoundException e) {
            throw new DAOException(e);
        }
    }
}
