package com.safelogic.autodex.web.configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.safelogic.autodex.web.model.AppConfig;
import com.safelogic.autodex.web.service.AppConfigService;

@Configuration
@Import(NaasDataSourceConfig.class)
@ComponentScan({"com.safelogic.autodex.web.*","com.safelogic.autodex.acpects"})

public class NaasAppConfig {
	
	public static Map<String, String> configMap = null;
	
	@Autowired
	private AppConfigService appConfigService;
	
	@Bean
	public Map<String, String> getAppConfig() {
		if(configMap == null) {
			
			List<AppConfig> appConfigList = appConfigService.getAllAppConfig();
			configMap = appConfigList.stream().collect(Collectors.toMap(AppConfig::getName, AppConfig::getValue)); 
		 
		}
		
		return configMap;
	}
	

    @Bean(name="passwordEncoder")
    public PasswordEncoder getEncoder(){
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("byheartlearner@gmail.com");
        javaMailSender.setPassword("XXX");
        javaMailSender.setJavaMailProperties(getMailProperties());
        return javaMailSender;
    }
    
    
    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.quitwait", "false");
        properties.setProperty("mail.debug", "true");
        return properties;
    }
    
    //TODO: get queue name from properties file or system property
    //Basically make config configurable...got meta quickly :)
    
    @Bean
    public JmsTemplate jmsQueueTemplate() throws IOException {
        Region region = Region.getRegion(Regions.US_EAST_1);
        SQSConnectionFactory.Builder builder = new SQSConnectionFactory.Builder()
                //.withAWSCredentialsProvider(new EnvironmentVariableCredentialsProvider())
                .withNumberOfMessagesToPrefetch(1)
                .withRegion(region);

        ConnectionFactory connectionFactory = builder.build();
        
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
        //jmsTemplate.setDefaultDestinationName(getAppConfig().get("aws.queue.notification.name"));
        jmsTemplate.setDefaultDestinationName("arn:aws:sqs:us-east-1:625617170453:naas_notification_dev");
        
        return jmsTemplate;
    }
}
