package by.ivanshilyaev.crawler.dao;

import by.ivanshilyaev.crawler.dao.exception.DAOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

/**
 * Convenience class for writing specified data into file.
 *
 * @version 1.0
 * @since 2020-07-06
 */

public class Top10ResultsWriter {
    /**
     * Writes specified data with prompts into file line by line.
     *
     * @param file file where the data is written.
     * @param map  a specified set of data to write.
     * @throws DAOException if file doesn't exist or not found.
     */
    public void writeResults(File file, Map<Integer, String> map) throws DAOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            int count = 0;
            Iterator<Map.Entry<Integer, String>> iterator = map.entrySet().iterator();
            while (count < 10) {
                if (iterator.hasNext()) {
                    Map.Entry<Integer, String> pair = iterator.next();
                    writer.println("Total hits: " + pair.getKey() + ", link: " + pair.getValue());
                    System.out.println("Total hits: " + pair.getKey() + ", link: " + pair.getValue());
                }
                ++count;
            }
        } catch (FileNotFoundException e) {
            throw new DAOException(e);
        }
    }
}
