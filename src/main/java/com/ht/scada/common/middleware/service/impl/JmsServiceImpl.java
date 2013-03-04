package com.ht.scada.common.middleware.service.impl;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueRequestor;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.management.JMSManagementHelper;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ht.scada.common.middleware.service.JmsService;

/**
 * JMS服务实现类<br>
 * 每末端都有一个对应的队列，读取队列中数据采用QueueBrowser获取<br>
 * 报警信息通过/topic/alarm进行推送<br>
 * 实时变化的信息通过/topic/data推送<br>
 * 
 * @author 薄成文
 * @author 赵磊
 */
@Service("jmsService")
public class JmsServiceImpl implements JmsService {

	private static final Logger log = LoggerFactory.getLogger(JmsServiceImpl.class);
	
	private static final String INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";
	private static final String PROVIDER_URL = "jnp://localhost:1099";
	private static final String URL_PKG_PREFIXES = "org.jboss.naming:org.jnp.interfaces";
	
	
	private Context initialContext;
	private HornetQConnectionFactory cf;
	private Connection connection;
	
	
	@PostConstruct
	private void initJndiJms() throws Exception {
		Properties prop = new Properties();
		prop.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, JmsServiceImpl.INITIAL_CONTEXT_FACTORY);
		prop.put(javax.naming.Context.URL_PKG_PREFIXES, JmsServiceImpl.URL_PKG_PREFIXES);
		prop.put(javax.naming.Context.PROVIDER_URL, JmsServiceImpl.PROVIDER_URL);

		initialContext = new InitialContext(prop);
		cf = (HornetQConnectionFactory) initialContext.lookup("/ConnectionFactory");
		connection = cf.createConnection();
		
		// TODO: 创建动态队列
		createQueue("queue1");
		createQueue("myqueue");
	}
	
	@PreDestroy
	private void destroy() {
		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cf.close();
	}
	
	private void createQueue(String queueName) throws JMSException {
		System.out.println("创建队列："+queueName);
		QueueConnection connection = ((QueueConnectionFactory)cf).createQueueConnection("admin", "admin123");
		connection.start();

		Queue managementQueue = HornetQJMSClient.createQueue("hornetq.management");
		QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		QueueRequestor requestor = new QueueRequestor(session, managementQueue);
		
		Message message = session.createMessage();
		
		JMSManagementHelper.putOperationInvocation(message, "jms.server", "createQueue", queueName, "/queue/" + queueName);
		Message reply = requestor.request(message);
		System.out.println(reply);
		
		session.close();
		connection.close();
	}
	
	@Override
	public void registTopicMessageListener(String topicPath, MessageListener listener) {
		try {
			Topic topic = (Topic) initialContext.lookup(topicPath);
			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			MessageConsumer redConsumer = session.createConsumer(topic);
			redConsumer.setMessageListener(listener);
			
			session.commit();
			connection.start();
			//session.close();
			//connection.close();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	

}
