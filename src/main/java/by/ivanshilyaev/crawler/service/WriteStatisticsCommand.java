package by.ivanshilyaev.crawler.service;

import by.ivanshilyaev.crawler.dao.DAOFactory;
import by.ivanshilyaev.crawler.dao.StatisticsWriter;
import by.ivanshilyaev.crawler.dao.exception.DAOException;
import by.ivanshilyaev.crawler.service.exception.ServiceException;

import java.io.File;
import java.util.Queue;

public class WriteStatisticsCommand {
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
