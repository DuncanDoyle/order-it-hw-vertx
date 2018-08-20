package org.jbpm.cases.vertx;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class BackendVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackendVerticle.class);
	
	private Map<String, JsonObject> orders = new HashMap<>();

	public BackendVerticle() {
		LOGGER.info("Initializing order data.");
		addOrder(new JsonObject().put("id", "order_1").put("info", "This is a Lenovo order."));
		addOrder(new JsonObject().put("id", "order_2").put("info", "This is an Apple order."));
	}
	
	
	@Override
	public void start() throws Exception {

		// A simple backend
		vertx.eventBus().<JsonObject>consumer("backend", msg -> {
			LOGGER.debug("Processing message.");
			JsonObject json = msg.body();
			
			switch (json.getString("op", "")) {
			case "get": {
				LOGGER.debug("Retrieving order: " + json.getString("id"));
				String orderID = json.getString("id");
				JsonObject order = orders.get(orderID);
				LOGGER.debug("Order: " + order);
				msg.reply(orders.get(orderID));
				break;
			}
			case "add": {
				String orderID = json.getString("id");
				JsonObject order = json.getJsonObject("order");
				order.put("id", orderID);
				msg.reply(addOrder(order));
				break;
			}
			case "list": {
				JsonArray arr = new JsonArray();
				orders.forEach((k, v) -> arr.add(v));
				msg.reply(arr);
				break;
			}
			default: {
				msg.fail(0, "operation not permitted");
			}
			}
		});
	}

	private boolean addOrder(JsonObject product) {
		//TODO: change logic for IT Order
		if (product.containsKey("info")) {
			orders.put(product.getString("id"), product);
			return true;
		} else {
			return false;
		}
	}

}
