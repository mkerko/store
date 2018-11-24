package com.wf.store.exceptions;

public class EntityWasNotFoundException extends Exception {
    public EntityWasNotFoundException() {
        super();
    }

    public EntityWasNotFoundException(String message) {
        super(message);
    }

    public EntityWasNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityWasNotFoundException(Throwable cause) {
        super(cause);
    }

    protected EntityWasNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
