package by.ivanshilyaev.crawler.dao;

import by.ivanshilyaev.crawler.dao.exception.DAOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Queue;

/**
 * Convenience class for writing specified data into file.
 *
 * @version 1.0
 * @since 2020-07-06
 */

public class StatisticsWriter {
    /**
     * Writes specified data into file line by line.
     *
     * @param file  file where the data is written.
     * @param queue a specified set of data to write.
     * @throws DAOException if file doesn't exist or not found.
     */
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
