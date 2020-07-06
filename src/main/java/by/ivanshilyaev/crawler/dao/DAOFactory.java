package by.ivanshilyaev.crawler.dao;

public class DAOFactory {
    private static final DAOFactory INSTANCE = new DAOFactory();
    private final StatisticsWriter statisticsWriter = new StatisticsWriter();

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return INSTANCE;
    }

    public StatisticsWriter getStatisticsWriter() {
        return statisticsWriter;
    }
}
