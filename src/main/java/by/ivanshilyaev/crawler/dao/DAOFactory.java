package by.ivanshilyaev.crawler.dao;

public final class DAOFactory {
    private static final DAOFactory INSTANCE = new DAOFactory();
    private final StatisticsWriter statisticsWriter = new StatisticsWriter();
    private final Top10ResultsWriter top10ResultsWriter = new Top10ResultsWriter();

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return INSTANCE;
    }

    public StatisticsWriter getStatisticsWriter() {
        return statisticsWriter;
    }

    public Top10ResultsWriter getTop10ResultsWriter() {
        return top10ResultsWriter;
    }
}
