package ru.otus.services;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.val;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class TemplateProcessorImpl implements TemplateProcessor {

    private final Configuration configuration;

    public TemplateProcessorImpl(String templatesDir) {
        configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setClassForTemplateLoading(this.getClass(), templatesDir);
        configuration.setDefaultEncoding("UTF-8");
    }

    @Override
    public String getPage(String filename, Map<String, Object> data) throws IOException {
        try (val stream = new StringWriter()) {
            val template = configuration.getTemplate(filename);
            template.process(data, stream);
            return stream.toString();
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }
}
