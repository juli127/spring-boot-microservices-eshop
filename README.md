# spring-boot-microservices-eshop
Spring Boot + Spring Cloud App

- Spring Boot 2.2.1
- Spring Data JPA
- Spring Cloud Netflix (Eureka, Hystrix, Config server, Ribbon)
- Swagger2 documentation API
- MySQL Server 5.7.23 
- HikariCP 3.1.0
- Lombok 1.16.22
- Gson 2.8.5
- slf4j 1.7.5, log4j 1.2.17
- Maven
- JDK 1.8

# Functionality description:
- 4 Domain microservices: user-service, product-service, cart-service, order-service
- discover-servise is Eureka server (port 8761)
- config-server (port 8888) loads application profiles from gitHub resource
- Hystrix, LoadBalancer are enabled for cart-service, order-service that calls other microservices
- REST requests for cart-service:
- - add {quantity} of product with {productId} to cart for user with {userId}:
POST localhost:8084/cart/users/{userId}/products/{productId}?quantity={quantity}
- - dlete {quantity} of product with {productId} from cart for user with {userId}:
DELETE localhost:8084/cart/users/{userId}/products/{productId}?quantity={quantity}
- - create/deleted cart for user with {userId}:
POST/DELETE localhost:8084/cart/users/{userId}
- REST requests for user-service:
- - get all users: GET localhost:8083/users
- - get user by Id: GET localhost:8083/users/{userId}
- - get user by login: GET localhost:8083/users?login={login}
- REST requests for product-service:
- - get all products: GET localhost:8082/products
- - get product by Id: GET localhost:8082/products/{productId}
- - get product by category: GET localhost:8082/products?category={category}
- - delete product by Id: DELETE localhost:8082/products/{productId}
- REST requests for order-service:
- - get all orders for user with userId: GET localhost:8081/orders/{userId}
- - delete all orders for user with userId: DELETE localhost:8081/orders/{userId}
