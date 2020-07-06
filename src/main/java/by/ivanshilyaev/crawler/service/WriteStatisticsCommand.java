package by.ivanshilyaev.crawler.service;

import by.ivanshilyaev.crawler.dao.DAOFactory;
import by.ivanshilyaev.crawler.dao.StatisticsWriter;
import by.ivanshilyaev.crawler.dao.exception.DAOException;
import by.ivanshilyaev.crawler.service.exception.ServiceException;

import java.io.File;
import java.util.Queue;

/**
 * Class that uses {@code StatisticsWriter} to write statistics into file.
 *
 * @version 1.0
 * @since 2020-07-06
 */

public class WriteStatisticsCommand {
    /**
     * Calls specified method to write data into file.
     *
     * @param file  file where the data is written.
     * @param queue a specified set of data to write.
     * @throws ServiceException if exception in dao layer occurs.
     */
    public void writeStatistics(File file, Queue<String> queue) throws ServiceException {
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            StatisticsWriter statisticsWriter = daoFactory.getStatisticsWriter();
            statisticsWriter.writeStatistics(file, queue);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
