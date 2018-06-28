package Logging;

import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.example.abylsen.AbylsenApiApplication;

public class LoggerManager {
	
	private static LoggerManager instance;
	
	public static LoggerManager getInstance() {
		if(instance == null)
			instance = new LoggerManager();
		
		return instance;
	}
	
	private LoggerManager() {
		initLogger();
	}
	
	private Logger logger;
	
	private void initLogger() {
		logger = Logger.getLogger(AbylsenApiApplication.class);
		logger.setAdditivity(false);
		try {
			logger.addAppender(new FileAppender(new PatternLayout("%d %-5p %c - %F:%L - %m%n"), "/AbylsenLog/server.log"));
		} catch (IOException e) {
			//Do nothing...
		};
	}
	
	public void logDebug(String msg) {
		if(logger == null)
			return;

		logger.log(Level.DEBUG, msg);
	}
	
	public void logError(String msg, Exception e) {
		if(logger == null)
			return;

		logger.log(Level.ERROR, msg);
		logger.log(Level.ERROR, e);
	}
}
