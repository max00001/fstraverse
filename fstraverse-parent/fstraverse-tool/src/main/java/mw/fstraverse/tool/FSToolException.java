package mw.fstraverse.tool;

public class FSToolException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1854426355190888016L;

    public FSToolException() {
    }

    public FSToolException(String message) {
        super(message);
    }

    public FSToolException(Throwable cause) {
        super(cause);
    }

    public FSToolException(String message, Throwable cause) {
        super(message, cause);
    }

    public FSToolException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
