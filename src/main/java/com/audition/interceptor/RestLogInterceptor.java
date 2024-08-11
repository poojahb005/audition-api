package com.audition.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class RestLogInterceptor implements ClientHttpRequestInterceptor {

    private final ObjectMapper objectMapper;

    public RestLogInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private static final Logger LOG = LoggerFactory.getLogger(RestLogInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
        throws IOException {

        String requestInfo = String.format("HEADERS: %s; METHOD: %s;", request.getHeaders(), request.getMethod());
        LOG.info("Rest call --- {}", request.getURI());
        LOG.info("Request details --- {}", requestInfo);

        ClientHttpResponse response = execution.execute(request, body);

        LOG.info("Response details --- STATUS: {};", response.getStatusCode());
        LOG.debug("BODY: {};",
            objectMapper.readValue(response.getBody(), Object.class));
        return response;
    }
}
