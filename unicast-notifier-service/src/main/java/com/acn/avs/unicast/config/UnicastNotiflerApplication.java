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
package com.acn.avs.unicast.config;

import java.io.FileNotFoundException;
import java.util.Locale;

import javax.sql.DataSource;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import com.acn.avs.unicast.exception.UnicastServiceInitializeException;
import com.acn.avs.unicast.message.UnicastJmsMessageListener;
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
@SpringBootApplication(scanBasePackages = { "com.acn.avs.unicast" })
@EnableJpaRepositories(basePackages = { "com.acn.avs.unicast.repository" })
@ComponentScan(basePackages = { "com.acn.avs.unicast" })
public class UnicastNotiflerApplication {

	private static final Logger LOGGER = Logger.getLogger(UnicastNotiflerApplication.class);

	private static final String UNICAST_JMS_QUEUE = "UNICAST_NOTIFIER_QUEUE";

	/** Rabbitmq host */
	@Value("${spring.rabbitmq.host}")
	private String rabbitmqHost;

	/** Rabbitmq port */
	@Value("${spring.rabbitmq.port}")
	private int rabbitmqPort;

	/** Rabbitmq username */
	@Value("${spring.rabbitmq.username}")
	private String rabbitmqUserName;

	/** Rabbitmq password */
	@Value("${spring.rabbitmq.password}")
	private String rabbitmqPassword;

	/** Rabbitmq concurrentConsumer */
	@Value("${rabbitmq.concurrentConsumer:1}")
	private int concurrentConsumer;

	/** Rabbitmq maxConcurrentConsumer */
	@Value("${rabbitmq.maxConcurrentConsumer:1}")
	private int maxConcurrentConsumer;

	/**
	 * serverPort value
	 */
	@Value("${server.port:0}")
	private int serverPort;
	
	/** ssl port */
	@Value("${ssl.port:0}")
	private int sslPort;

	/** ssl protocol */
	@Value("${ssl.protocol:TLS}")
	private String sslProtocol;

	/** ssl keystore password */
	@Value("${ssl.keystore.password:unicast}")
	private String keystorePassword;

	/** ssl keystore alias */
	@Value("${ssl.keystore.alias:unicast}")
	private String keystoreAlias;

	/** ssl keystore filePath */
	@Value("${ssl.keystore.filePath:keystore/unicastkeystore.jks}")
	private String keystoreFilePath;

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
	 * Bean for RabbitMQ connection factory
	 * 
	 * @return
	 */
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitmqHost, rabbitmqPort);
		connectionFactory.setUsername(rabbitmqUserName);
		connectionFactory.setPassword(rabbitmqPassword);

		return connectionFactory;
	}

	/**
	 * Bean for JMS queue
	 * 
	 * @return Queue
	 */
	@Bean
	public Queue simpleQueue() {
		return new Queue(UNICAST_JMS_QUEUE);
	}

	/**
	 * Bean for RabbitMQ Template
	 * 
	 * @return RabbitTemplate
	 */
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setRoutingKey(UNICAST_JMS_QUEUE);
		return template;
	}

	/**
	 * Bean for RabbitMQ Container
	 * 
	 * @return SimpleMessageListenerContainer
	 */
	@Bean
	SimpleMessageListenerContainer container() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueues(simpleQueue());
		container.setMessageListener(listenerAdapter(unicastJmsMessageListener()));
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		container.setDefaultRequeueRejected(false);
		container.setConcurrentConsumers(concurrentConsumer);
		container.setMaxConcurrentConsumers(maxConcurrentConsumer);
		LOGGER.info("SimpleMessageListenerContainer initialized with concurrentConsumer: "+ concurrentConsumer+ ", maxConcurrentConsumer:"+ maxConcurrentConsumer);
		return container;
	}

	/**
	 * Bean for unicastRequest JMS message Listener
	 * 
	 * @return UnicastJmsMessageListener
	 */
	@Bean
	UnicastJmsMessageListener unicastJmsMessageListener() {
		return new UnicastJmsMessageListener();
	}

	/**
	 * Bean for message adapter which adds message listener class
	 * 'UnicastJmsMessageListener'
	 * 
	 * @param unicastJmsMessageListener
	 * @return
	 */
	@Bean
	MessageListenerAdapter listenerAdapter(UnicastJmsMessageListener unicastJmsMessageListener) {
		return new MessageListenerAdapter(unicastJmsMessageListener, "onMessage");
	}

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(UnicastNotiflerApplication.class, args);
	}

	/**
	 * Bean for adding Tomcat as the embedded Servlet Container and creating
	 * Factory
	 * 
	 * @return EmbeddedServletContainerFactory
	 * @throws FileNotFoundException
	 */
	@Bean
	public EmbeddedServletContainerFactory servletContainer() throws FileNotFoundException {
		
		if (serverPort == 0 ){
			throw new UnicastServiceInitializeException("Could not find any valid http port to bind. To switch off the HTTP endpoints completely server.port needs to configure with -1.");
		}
		
		if (serverPort <= 0 && sslPort <= 0){
			throw new UnicastServiceInitializeException("Could not find any valid port to bind.");
		}
		
		TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory = new TomcatEmbeddedServletContainerFactory();
		if (sslPort >0){
			tomcatEmbeddedServletContainerFactory.addAdditionalTomcatConnectors(createSslConnector());
		}else {
			LOGGER.info("ssl port would not configured, so service will be available only on http port.");
		}
		return tomcatEmbeddedServletContainerFactory;
	}

	/**
	 * SSL connector
	 * 
	 * @return Connector
	 * @throws FileNotFoundException
	 */
	private Connector createSslConnector() throws FileNotFoundException {
		Connector connector = new Connector(Http11NioProtocol.class.getName());
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
		connector.setPort(sslPort);
		connector.setSecure(true);
		connector.setScheme("https");
		protocol.setSSLEnabled(true);
		protocol.setKeyAlias(keystoreAlias);
		protocol.setKeystorePass(keystorePassword);
		protocol.setKeystoreFile(ResourceUtils.getFile(keystoreFilePath).getAbsolutePath());
		protocol.setSslProtocol(sslProtocol);
		LOGGER.info("SSLConnector bind with sslPort:" + sslPort + ", keystoreAlias: "+ keystoreAlias+ ", sslProtocol: "+ sslProtocol + ", keystoreFilePath: "+ keystoreFilePath);
		return connector;
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
