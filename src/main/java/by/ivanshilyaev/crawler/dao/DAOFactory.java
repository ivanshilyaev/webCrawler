package by.ivanshilyaev.crawler.dao;

/**
 * Singleton class-factory that gives access to other classes in this package.
 *
 * @version 1.0
 * @since 2020-07-06
 */

public final class DAOFactory {
    private static final DAOFactory INSTANCE = new DAOFactory();
    private final StatisticsWriter statisticsWriter = new StatisticsWriter();
    private final Top10ResultsWriter top10ResultsWriter = new Top10ResultsWriter();

    private DAOFactory() {
    }

    /**
     * Instance getter.
     *
     * @return instance of this class.
     */
    public static DAOFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Getter for {@code StatisticsWriter} instance.
     *
     * @return instance of {@code StatisticsWriter}.
     */
    public StatisticsWriter getStatisticsWriter() {
        return statisticsWriter;
    }

    /**
     * Getter for {@code Top10ResultsWriter} instance.
     *
     * @return instance of {@code Top10ResultsWriter}.
     */
    public Top10ResultsWriter getTop10ResultsWriter() {
        return top10ResultsWriter;
    }
}
