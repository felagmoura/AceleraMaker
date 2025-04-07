/**
 * BaseController provides common functionality for controllers in the application.
 * It includes constants for API versioning and path, as well as pagination utilities.
 * 
 * <p>Constants:
 * <ul>
 *   <li>{@code API_VERSION} - The version of the API.</li>
 *   <li>{@code API_PATH} - The base path for the API.</li>
 *   <li>{@code DEFAULT_PAGE_SIZE} - The default number of items per page, configurable via application properties.</li>
 *   <li>{@code DEFAULT_FIRST_PAGE} - The default starting page index (0-based).</li>
 * </ul>
 * 
 * <p>Methods:
 * <ul>
 *   <li>{@link #paginate(Integer, Integer, String)} - Creates a {@link Pageable} object for pagination based on the provided parameters.</li>
 * </ul>
 * 
 * <p>Usage:
 * <pre>
 * {@code
 * Pageable pageable = baseController.paginate(1, 10, "name,asc");
 * }
 * </pre>
 * 
 * <p>Pagination Parameters:
 * <ul>
 *   <li>{@code page} - The page number to retrieve (0-based). Defaults to {@code DEFAULT_FIRST_PAGE} if null.</li>
 *   <li>{@code perPage} - The number of items per page. Defaults to {@code DEFAULT_PAGE_SIZE} if null.</li>
 *   <li>{@code sort} - A comma-separated string specifying the sorting fields and directions (e.g., "field,asc").</li>
 * </ul>
 */
package com.montreal.acelera.blog_pessoal.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
public class BaseController {
    protected static final String API_VERSION = "v1";
    protected static final String API_PATH = "api";

    @Value("${default-page-size}")
    protected int DEFAULT_PAGE_SIZE;
    protected static final int DEFAULT_FIRST_PAGE = 0;

    /**
     * Creates a {@link Pageable} object for pagination and sorting.
     *
     * @param page    The page number to retrieve. If null, defaults to the first
     *                page.
     * @param perPage The number of items per page. If null, defaults to the default
     *                page size.
     * @param sort    A comma-separated string specifying the sorting fields. If
     *                null or empty, no sorting is applied.
     * @return A {@link Pageable} object configured with the specified page, size,
     *         and sorting options.
     */
    protected Pageable paginate(Integer page, Integer perPage, String sort) {
        Pageable pageable = PageRequest
                .of(page == null ? DEFAULT_FIRST_PAGE : page, perPage == null ? DEFAULT_PAGE_SIZE : perPage);
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            pageable = PageRequest.of(page == null ? DEFAULT_FIRST_PAGE : page,
                    perPage == null ? DEFAULT_PAGE_SIZE : perPage, Sort.by(sortParams));
        }
        return pageable;
    }
}
