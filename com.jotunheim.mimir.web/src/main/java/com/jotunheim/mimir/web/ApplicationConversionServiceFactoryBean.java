package com.jotunheim.mimir.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

/**
 * A central place to register application converters and formatters.
 */
public class ApplicationConversionServiceFactoryBean extends
        FormattingConversionServiceFactoryBean {

    private static org.apache.log4j.Logger LOG = Logger
            .getLogger(ApplicationConversionServiceFactoryBean.class);

    @SuppressWarnings("deprecation")
    @Override
    protected void installFormatters(FormatterRegistry registry) {
        super.installFormatters(registry);
        // Register application converters and formatters
        registry.addConverter(getString2DateConverter());
    }

    public Converter<String, Date> getString2DateConverter() {
        return new Converter<String, Date>() {

            @Override
            public Date convert(String source) {
                LOG.debug("Converting String to Date: " + source);
                try {
                    if (null == source || "".equals(source)) {
                        return new Date();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return (sdf.parse(source));
                } catch (Exception ex) {
                    LOG.error(ex.getMessage(), ex);
                    return new Date();
                }
            }
        };
    }
}
