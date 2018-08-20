package org.jbpm.cases.vertx;


import static io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
//import io.vertx.core.logging.Logger;
//import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.SLF4JLogDelegateFactory;

/**
 * Startup verticle. Bootstraps all other verticles.
 * 
 * 
 * @author <a href="mailto:duncan.doyle@redhat.com">Duncan Doyle</a>
 */
public class MainVerticle extends AbstractVerticle {
	
	static {
		System.setProperty (LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory.class.getName ());
	    LoggerFactory.getLogger (LoggerFactory.class); // Required for Logback to work in Vertx
	}
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);
	
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		LOGGER.info("Starting Main Verticle.");

		// First register our MessageCodecs on the EventBus.
		registerCodecs();

		// This is the main verticle, so setting our own Config.
		DeploymentOptions deploymentOptions = new DeploymentOptions().setConfig(setConfig());
		
		vertx.deployVerticle(ServerVerticle.class.getName());
		vertx.deployVerticle(BackendVerticle.class.getName());
		
		
		startFuture.complete();
	}
	
	private JsonObject setConfig() {
		//LOGGER.info("Setting Config!");
		//return context.config().put(Constants.KJAR_GROUPID_CONFIG_KEY, "org.drools.demo").put(Constants.KJAR_ARTIFACTID_CONFIG_KEY, "drools-cep-vertx-demo-kjar")
		//		.put(Constants.KJAR_VERSION_CONFIG_KEY, "0.0.1-SNAPSHOT");
		return context.config();
	}
	
	private void registerCodecs() {
		//vertx.eventBus().registerDefaultCodec(Command.class, new CommandMessageCodec());
		//vertx.eventBus().registerDefaultCodec(FlightInfoEvent.class, new FlightInfoCodec());
	}
	

}
