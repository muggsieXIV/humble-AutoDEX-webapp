package com.safelogic.autodex.web.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:jdbc.properties")
@EnableTransactionManagement
public class NaasDataSourceConfig {

	@Bean
	@Autowired
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPersistenceXmlLocation("classpath:META-INF/persistence.xml");
		//em.setPersistenceUnitName("autodecker-entity-mapping");
		em.setPersistenceUnitName("autodex-entity-mapping");
		return em;
	}

	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emfBean) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emfBean.getObject());
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	@Bean(name = "dataSourceFactory")
	public JndiObjectFactoryBean demoDataSource() {
	    JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
	    factory.setJndiName("java:comp/env/jdbc/AutodexDS"); // tomcat
	    //factory.setJndiName("java:/datasources/NaasDS"); // jboss
	    factory.setProxyInterface(DataSource.class);
	    return factory;
	}
	
	@Bean(name = "dataSource")
	@Autowired
	public DataSource getDataSource(JndiObjectFactoryBean demoDataSource) {
	     
		return (DataSource)demoDataSource.getObject();
	}
	
}
