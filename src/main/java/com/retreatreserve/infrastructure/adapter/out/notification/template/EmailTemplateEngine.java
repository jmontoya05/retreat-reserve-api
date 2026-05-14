package com.retreatreserve.infrastructure.adapter.out.notification.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.retreatreserve.infrastructure.exception.notification.EmailTemplateProcessingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Slf4j
public class EmailTemplateEngine {
    
    private static final String TEMPLATE_PATH = "templates/email/";
    
    public String processTemplate(String templateName, Map<String, String> variables) {
        try {
            String template = loadTemplate(templateName);
            return replaceVariables(template, variables);
        } catch (IOException e) {
            log.error("Failed to process email template: {}", templateName, e);
            throw new EmailTemplateProcessingException("Failed to process email template", e);
        }
    }
    
    private String loadTemplate(String templateName) throws IOException {
        ClassPathResource resource = new ClassPathResource(TEMPLATE_PATH + templateName);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
    
    private String replaceVariables(String template, Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            result = result.replace(placeholder, entry.getValue());
        }
        return result;
    }
}
