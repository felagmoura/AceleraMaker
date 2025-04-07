/**
 * Custom exception class to handle invalid requests in the application.
 * This exception is typically thrown when a request does not meet the
 * required validation criteria or contains invalid data.
 *
 * Package: com.montreal.acelera.blog_pessoal.exeception
 */
package com.montreal.acelera.blog_pessoal.exeception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidRequestException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }
}
