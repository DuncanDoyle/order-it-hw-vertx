package org.jbpm.cases.vertx.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@Path("/order")
public class OrderResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderResource.class);
	
	@GET
	@Path("/{orderID}")
	@Produces({ MediaType.APPLICATION_JSON })
	public void getOrder(@Suspended final AsyncResponse asyncResponse, 
										@Context Vertx vertx,
										@PathParam("orderID") String orderID) {

		if (orderID == null) {
			asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
			return;
		}

		LOGGER.debug("Sending 'get' request to backend for order: " + orderID);
		// Send a get message to the backend
		vertx.eventBus().<JsonObject>send("backend", new JsonObject().put("op", "get").put("id", orderID), msg -> {
			// When we get the response we resume the Jax-RS async response
			if (msg.succeeded()) {
				LOGGER.debug("Got reponse: " + msg.result().body());
				JsonObject json = msg.result().body();
				if (json != null) {
					asyncResponse.resume(json.encode());
				} else {
					asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
				}
			} else {
				asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
			}
		});
	}

}
