package de.spurtikus.vxi.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@WebListener
public class WebAppStartStopListener implements ServletContextListener {
	static Logger logger = LoggerFactory
			.getLogger(WebAppStartStopListener.class);

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger.info("Servlet has been started.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger.info("Servlet has been stopped.");
	}
}
