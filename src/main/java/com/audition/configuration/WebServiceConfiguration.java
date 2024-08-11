package com.audition.configuration;

import com.audition.interceptor.ResponseHeaderInjector;
import com.audition.interceptor.RestLogInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebServiceConfiguration implements WebMvcConfigurer {

    private static final String YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd";

    /**
     * custom object mapper configuration with, 1. allows for date format as yyyy-MM-dd, 2. Does not fail on unknown
     * properties, 3. maps to camelCase, 4. Does not include null values or empty values, 5. does not write datas as
     * timestamps.
     *
     * @return object mapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(YEAR_MONTH_DAY_PATTERN))
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
            .setSerializationInclusion(Include.NON_EMPTY)
            .setSerializationInclusion(Include.NON_NULL);
        return objectMapper;
    }

    /**
     * @return rest template
     */
    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate(
            new BufferingClientHttpRequestFactory(createClientFactory()));
        // use object mapper
        restTemplate.getMessageConverters().forEach(mc -> {
            if (mc instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) mc).setObjectMapper(objectMapper());
            }
        });
        //  create a logging interceptor that logs request/response for rest template calls.
        List<ClientHttpRequestInterceptor> restInterceptors = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(restInterceptors)) {
            restInterceptors = new ArrayList<>();
        }
        restInterceptors.add(restLogInterceptor());
        restTemplate.setInterceptors(restInterceptors);
        return restTemplate;
    }

    @Bean("logInterceptor")
    public RestLogInterceptor restLogInterceptor() {
        return new RestLogInterceptor(objectMapper());
    }

    @Bean("responseInjector")
    public ResponseHeaderInjector responseHeaderInjector() {
        return new ResponseHeaderInjector();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(responseHeaderInjector());
    }

    private SimpleClientHttpRequestFactory createClientFactory() {
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        return requestFactory;
    }
}
