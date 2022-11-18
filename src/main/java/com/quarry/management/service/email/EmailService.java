package com.quarry.management.service.email;

import com.quarry.management.entity.Employee;
import com.quarry.management.entity.Manager;
import com.quarry.management.utils.AppUtils;
import com.quarry.management.utils.Constants;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource(value = {"classpath:application.yml"})
public class EmailService {

    public static final String MAIL_FOLDER_PATH = "mail/";

    @Value("${spring.robot.email.from}")
    private String from;

    @Autowired()
    private JavaMailSender emailSender;

    @Qualifier("freeMarkerConfiguration")
    @Autowired
    private Configuration freemarkerConfig;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    public void sendSimpleMessageByTemplate(Mail mail, String templateName) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template template = freemarkerConfig.getTemplate(MAIL_FOLDER_PATH + templateName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
            helper.setTo(mail.getTo());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(from);
            emailSender.send(message);
            LOGGER.info("Email send!");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Async
    public void sendEmployeePasswordNotification(Employee employee, String password) {
        try {
            Map<String, String> model = new HashMap<>();
            model.put(Constants.USERNAME, AppUtils.capitalize(employee.getName()));
            model.put(Constants.EMAIL, employee.getEmail());
            model.put(Constants.PASSWORD, password);
            Mail mail = new Mail();
            mail.setSubject(Constants.ACCOUNT_CREATION_NOTIFICATION);
            mail.setTo(employee.getEmail());
            mail.setModel(model);
            sendSimpleMessageByTemplate(mail, Constants.EMPLOYEE_PASSWORD_TEMPLATE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Async
    public void sendManagerPasswordNotification(Manager manager, String password) {
        try {
            Map<String, String> model = new HashMap<>();
            model.put(Constants.USERNAME, AppUtils.capitalize(manager.getName()));
            model.put(Constants.EMAIL, manager.getEmail());
            model.put(Constants.PASSWORD, password);
            Mail mail = new Mail();
            mail.setSubject(Constants.ACCOUNT_CREATION_NOTIFICATION);
            mail.setTo(manager.getEmail());
            mail.setModel(model);
            sendSimpleMessageByTemplate(mail, Constants.MANAGER_PASSWORD_TEMPLATE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}