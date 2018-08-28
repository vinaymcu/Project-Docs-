package com.acn.avs.unicast.config;

/***************************************************************************
 * Copyright (C) Accenture
 * 
 * The reproduction, transmission or use of this document or its contents is not permitted without
 * prior express written consent of Accenture. Offenders will be liable for damages. All rights,
 * including but not limited to rights created by patent grant or registration of a utility model or
 * design, are reserved.
 * 
 * Accenture reserves the right to modify technical specifications and features.
 * 
 * Technical specifications and features are binding only insofar as they are specifically and
 * expressly agreed upon in a written contract.
 * 
 **************************************************************************/

import java.util.Locale;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

/**
 * UnicastNotiflerApplication is the base class.
 *
 */
@Configuration
@EnableHystrix
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = { RabbitAutoConfiguration.class })
@EntityScan(basePackages = { "com.acn.avs.unicast.entity" })
//@SpringBootApplication(scanBasePackages = { "com.acn.avs.unicast" })
@EnableJpaRepositories(basePackages = { "com.acn.avs.unicast.repository" })
//@ComponentScan(basePackages = { "com.acn.avs.unicast" })
public class TestConfig {

	private static final Logger LOGGER = Logger.getLogger(TestConfig.class);

	/**
	 * serverPort value
	 */
	@Value("${server.port:0}")
	private int serverPort;
	

	/** Trigger server ConnectionTimeOut */
	@Value("${tqs.server.connection.timeout:2000}")
	private int tqsConnectionTimeOut;

	/** Trigger server ReadTimeOut */
	@Value("${tqs.server.read.timeout:1000}")
	private int tqsReadTimeOut;

	/**
	 * Bean to laod resouce bundle message source file
	 * 
	 * @return ReloadableResourceBundleMessageSource
	 */
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages/message");
		return messageSource;
	}

	/**
	 * Bean to read message resource with english as locale
	 * 
	 * @return MessageSourceAccessor
	 */
	@Bean
	public MessageSourceAccessor messageSourceAccessor() {
		return new MessageSourceAccessor(messageSource(), Locale.ENGLISH);
	}


	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(TestConfig.class, args);
	}


	/**
	 * Bean to build primary data source
	 * 
	 * @return DataSource
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	@Primary
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * Bean to build stb manager data source
	 * 
	 * @return DataSource
	 */
	@Bean(name = "stbmanagerDb")
	@ConfigurationProperties(prefix = "spring.stbmanager.datasource")
	public DataSource mysqlDataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * Bean for Jdbc template
	 * 
	 * @param dsMySQL
	 * @return JdbcTemplate
	 */
	@Bean(name = "stbmanagerJdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("stbmanagerDb") DataSource dsMySQL) {
		return new JdbcTemplate(dsMySQL);
	}

	/**
	 * Bean for Json data reader using gson
	 * 
	 * @return Gson
	 */
	@Bean
	public Gson getGson() {
		return new Gson();
	}

	/**
	 * Bean for http client request factory
	 * 
	 * @return SimpleClientHttpRequestFactory
	 */
	@Bean(name = "simpleClientHttpRequestFactory")
	public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		simpleClientHttpRequestFactory.setConnectTimeout(tqsConnectionTimeOut);
		simpleClientHttpRequestFactory.setReadTimeout(tqsReadTimeOut);
		return simpleClientHttpRequestFactory;
	}

	/**
	 * Bean for RestTemplte
	 * 
	 * @param simpleClientHttpRequestFactory
	 * @return RestTemplate
	 */
	@Bean
	public RestTemplate getRestTemplate(
			@Qualifier("simpleClientHttpRequestFactory") SimpleClientHttpRequestFactory simpleClientHttpRequestFactory) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
		return restTemplate;
	}
}

