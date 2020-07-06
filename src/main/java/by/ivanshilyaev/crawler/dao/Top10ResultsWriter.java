package by.ivanshilyaev.crawler.dao;

import by.ivanshilyaev.crawler.dao.exception.DAOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

public class Top10ResultsWriter {
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
