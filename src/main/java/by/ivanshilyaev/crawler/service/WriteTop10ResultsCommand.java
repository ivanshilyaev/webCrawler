package by.ivanshilyaev.crawler.service;

import by.ivanshilyaev.crawler.dao.DAOFactory;
import by.ivanshilyaev.crawler.dao.Top10ResultsWriter;
import by.ivanshilyaev.crawler.dao.exception.DAOException;
import by.ivanshilyaev.crawler.service.exception.ServiceException;

import java.io.File;
import java.util.Map;

public class WriteTop10ResultsCommand {
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
