package org.jbpm.cases.vertx;

import org.jboss.resteasy.plugins.server.vertx.VertxRequestHandler;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;
import org.jbpm.cases.vertx.jaxrs.OrderResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;

public class ServerVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);
	
	@Override
	public void start() throws Exception {

		// Build the Jax-RS controller deployment
		VertxResteasyDeployment deployment = new VertxResteasyDeployment();
		deployment.start();
		deployment.getRegistry().addPerInstanceResource(OrderResource.class);

		// Start the front end server using the Jax-RS controller
		vertx.createHttpServer().requestHandler(new VertxRequestHandler(vertx, deployment)).listen(8080, ar -> {
			System.out.println("Server started on port " + ar.result().actualPort());
		});
	}
}
