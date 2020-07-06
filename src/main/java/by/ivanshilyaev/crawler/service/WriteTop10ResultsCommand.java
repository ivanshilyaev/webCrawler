package by.ivanshilyaev.crawler.service;

import by.ivanshilyaev.crawler.dao.DAOFactory;
import by.ivanshilyaev.crawler.dao.Top10ResultsWriter;
import by.ivanshilyaev.crawler.dao.exception.DAOException;
import by.ivanshilyaev.crawler.service.exception.ServiceException;

import java.io.File;
import java.util.Map;

/**
 * Class that uses {@code Top10ResultsWriter} to write statistics into file.
 *
 * @version 1.0
 * @since 2020-07-06
 */

public class WriteTop10ResultsCommand {
    /**
     * Calls specified method to write data into file.
     *
     * @param file file where the data is written.
     * @param map  a specified set of data to write.
     * @throws ServiceException if exception in dao layer occurs.
     */
    public void writeResults(File file, Map<Integer, String> map) throws ServiceException {
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            Top10ResultsWriter top10ResultsWriter = daoFactory.getTop10ResultsWriter();
            top10ResultsWriter.writeResults(file, map);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
