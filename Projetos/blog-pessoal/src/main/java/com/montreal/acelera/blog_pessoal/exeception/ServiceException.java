/**
 * This class represents a custom exception for the service layer of the application.
 * It is used to handle and encapsulate specific error scenarios that occur during
 * the execution of business logic in the blog application.
 *
 * Package: com.montreal.acelera.blog_pessoal.exeception
 */
package com.montreal.acelera.blog_pessoal.exeception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceException extends Exception {
    private static final long serialVersionUID = 1L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

}
