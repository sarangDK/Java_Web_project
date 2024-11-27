package ca.gbc.apigateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;


@Configuration
@Slf4j
public class Routes {

    @Value("${service.approval.url}")
    private String approvalServiceUrl;

    @Value("${service.booking.url}")
    private String bookingServiceUrl;

    @Value("${service.event.url}")
    private String eventServiceUrl;
    @Value("${service.room.url}")
    private String roomServiceUrl;
    @Value("${service.user.url}")
    private String userServiceUrl;


    @Bean
    public RouterFunction<ServerResponse> approvalServiceRoute() {
        log.info("Initializing approval service route with URL: {}", approvalServiceUrl);
        return GatewayRouterFunctions.route("approval_service")
                .route(RequestPredicates.path("/api/v1/approval"), HandlerFunctions.http(approvalServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("approvalServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        log.info("Initializing order service route with URL: {}", bookingServiceUrl);
        return GatewayRouterFunctions.route("order_service")
                .route(RequestPredicates.path("/api/order"), HandlerFunctions.http(bookingServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> eventServiceRoute() {
        log.info("Initializing event service route with URL: {}", eventServiceUrl);
        return GatewayRouterFunctions.route("event_service")
                .route(RequestPredicates.path("/api/v1/event"), HandlerFunctions.http(eventServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> roomServiceRoute() {
        log.info("Initializing room service route with URL: {}", roomServiceUrl);
        return GatewayRouterFunctions.route("room_service")
                .route(RequestPredicates.path("/api/v1/room"), HandlerFunctions.http(roomServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        log.info("Initializing user service route with URL: {}", userServiceUrl);
        return GatewayRouterFunctions.route("user_service")
                .route(RequestPredicates.path("/api/v1/user"), HandlerFunctions.http(userServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> approvalServiceSwaggerRoute() {
        return route("approval_service_swagger")
                .route(RequestPredicates.path("/aggregate/approval-service/v3/api-docs"),
                        http(approvalServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("approvalServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> bookingServiceSwaggerRoute() {
        return route("booking_service_swagger")
                .route(RequestPredicates.path("/aggregate/booking-service/v3/api-docs"),
                        http(bookingServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("bookingServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> eventServiceSwaggerRoute() {
        return route("event_service_swagger")
                .route(RequestPredicates.path("/aggregate/event-service/v3/api-docs"),
                        http(eventServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("eventServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> roomServiceSwaggerRoute() {
        return route("room_service_swagger")
                .route(RequestPredicates.path("/aggregate/room-service/v3/api-docs"),
                        http(roomServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("roomServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceSwaggerRoute() {
        return route("user_service_swagger")
                .route(RequestPredicates.path("/aggregate/user-service/v3/api-docs"),
                        http(userServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("userServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service Unavailable, please try again"))
                .build();
    }

}
