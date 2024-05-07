package com.santander.bp.infra.exception;

import com.santander.ars.error.conventions.HttpCode;
import com.santander.ars.error.catalog.ErrorCode;

/**
 * Enum representing errors and their associated codes and messages.
 */
public enum AppArsenalErrorCode implements ErrorCode {

    ERROR_APPARSENAL_NOT_FOUND(1001,HttpCode.NOT_FOUND,"apparsenal.not_found"),
    ERROR_APPARSENAL_ALREADY_EXISTS(1002,HttpCode.BAD_REQUEST,"apparsenal.exists");

    /**
     * Error properties.
     */
    private final Properties properties;

    /**
     * Constructor with parameters.
     *
     * @param errorCode The error code.
     * @param httpCode The associated HTTP code.
     * @param message The error message.
     */
    AppArsenalErrorCode(int errorCode,HttpCode httpCode,String message) {
        this.properties=Properties.of(errorCode,httpCode,this.name(),message);
    }

    /** Getter properties. */
    @Override
    public Properties getProperties() {
        return properties;
    }
}
