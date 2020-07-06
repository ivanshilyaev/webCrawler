package by.ivanshilyaev.crawler.dao.exception;

/**
 * Thrown when an error in reading or writing to the source (file, database, etc.) occurred.
 *
 * @version 1.0
 * @since 2020-07-06
 */

public class DAOException extends Exception {
    /**
     * Constructs a DAOException with no detail message.
     */
    public DAOException() {
    }

    /**
     * Constructs a DAOException with the specified detail message.
     *
     * @param message the detail message.
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Constructs a DAOException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a DAOException with the specified cause.
     *
     * @param cause the cause.
     */
    public DAOException(Throwable cause) {
        super(cause);
    }
}
