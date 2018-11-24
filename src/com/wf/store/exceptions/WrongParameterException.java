package com.wf.store.exceptions;

public class WrongParameterException extends Exception {
    public WrongParameterException() {
        super();
    }

    public WrongParameterException(String message) {
        super(message);
    }

    public WrongParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongParameterException(Throwable cause) {
        super(cause);
    }

    protected WrongParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
