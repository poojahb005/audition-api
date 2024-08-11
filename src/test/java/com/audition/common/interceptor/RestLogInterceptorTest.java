package com.audition.common.interceptor;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doReturn;

import com.audition.interceptor.RestLogInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

@ExtendWith(MockitoExtension.class)
public class RestLogInterceptorTest {

    @Mock
    private HttpHeaders mockHttpHeaders;

    @Mock
    private HttpRequest mockHttpRequest;

    @Mock
    private ClientHttpRequestExecution mockClientHttpRequestExecution;

    @Mock
    private ClientHttpResponse mockClientHttpResponse;

    @InjectMocks
    RestLogInterceptor restLogInterceptor = new RestLogInterceptor(new ObjectMapper());

    @Test
    void shouldGetPostsForQueryParamNull() throws Exception {
        doReturn(mockHttpHeaders).when(mockHttpRequest).getHeaders();
        doReturn(mockClientHttpResponse).when(mockClientHttpRequestExecution).execute(mockHttpRequest, new byte[1]);
        doReturn(new ByteArrayInputStream("{\"id\":\"1\"}".getBytes())).when(mockClientHttpResponse).getBody();
        ClientHttpResponse actualResult = restLogInterceptor.intercept(mockHttpRequest, new byte[1],
            mockClientHttpRequestExecution);
        assertSame(mockClientHttpResponse, actualResult);
    }

}
