package com.projects.order_ms.services;

import com.projects.order_ms.dtos.*;
import com.projects.order_ms.exceptions.OrderNotFoundException;
import com.projects.order_ms.exceptions.ProductNotAvailableException;
import com.projects.order_ms.models.Order;
import com.projects.order_ms.models.OrderProduct;
import com.projects.order_ms.models.OrderStatus;
import com.projects.order_ms.repositories.OrderProductRepository;
import com.projects.order_ms.repositories.OrderRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderSerivceImpl implements OrderService {

    private final WebClient webClient;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final String BASE_URL = "http://localhost:8080/products";

    @Autowired
    public OrderSerivceImpl(WebClient webClient, OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.webClient = webClient;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public Order createOrder(long userId, List<OrderDetail> orderDetails) throws ProductNotAvailableException {
        // Check for Product Availability
        // If products are available create the Order
        // Get Products
        List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).toList();
        List<ProductDTO> products = getProductByIds(productIds);
        Map<Long, ProductDTO> productMap = getProductMap(orderDetails, products);
        if(products == null || products.size() != orderDetails.size()) throw new ProductNotAvailableException("Some products are not available");
        if(!checkForAvailableQuantity(products, orderDetails)) {
            throw new ProductNotAvailableException("Stock is not available");
        }
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(new Date());
        order.setOrderStatus(OrderStatus.PLACED);
        double totalAmount = getTotalAmount(orderDetails, productMap);
        order.setTotalAmount(totalAmount);
        return this.orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    @Override
    public Order getOrderById(long orderId) throws OrderNotFoundException {
        return getOrder(orderId);
    }

    @Override
    public void deleteOrder(long orderId) throws OrderNotFoundException {
        Order order = getOrder(orderId);
        this.orderRepository.delete(order);
    }

    private Order getOrder(long orderId) throws OrderNotFoundException {
        Optional<Order> orderOptional = this.orderRepository.findById(orderId);
        if(orderOptional.isEmpty()) throw new OrderNotFoundException("Invalid Order ID");
        return orderOptional.get();
    }

    private List<ProductDTO> getProductByIds(List<Long> productIds) {
        GetProductsRequestDTO requestDTO = new GetProductsRequestDTO();
        requestDTO.setProductIds(productIds);
        return this.webClient.post()
                .uri(BASE_URL + "/details")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestDTO))
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .collectList()
                .block();
    }

    private boolean checkForAvailableQuantity(List<ProductDTO> products, List<OrderDetail> orderDetails) {
        for(ProductDTO product: products) {
            for(OrderDetail orderDetail: orderDetails) {
                if(product.getId() == orderDetail.getProductId()) {
                    if(orderDetail.getQuantity() > product.getAvailableQuantity()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private double getTotalAmount(List<OrderDetail> orderDetails, Map<Long, ProductDTO> productMap) {
        double totalAmount = 0;
        for(OrderDetail orderDetail: orderDetails) {
            int quantity = orderDetail.getQuantity();
            double price = productMap.get(orderDetail.getProductId()).getPrice();
            totalAmount += quantity * price;
        }
        return totalAmount;
    }

    @Override
    public void updateOrderProducts(List<OrderDetail> orderDetails, Order order) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        for(OrderDetail orderDetail: orderDetails) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderId(order.getId());
            orderProduct.setProductId(orderDetail.getProductId());
            orderProduct.setQuantity(orderDetail.getQuantity());
            orderProducts.add(orderProduct);
        }
        this.orderProductRepository.saveAll(orderProducts);
    }

    private Map<Long, ProductDTO> getProductMap(List<OrderDetail> orderDetails, List<ProductDTO> products) {
        Map<Long, ProductDTO> productMap = new HashMap<>();
        for(OrderDetail orderDetail: orderDetails) {
            for(ProductDTO product: products) {
                if(product.getId() == orderDetail.getProductId()) {
                    productMap.put(product.getId(), product);
                }
            }
        }
        return productMap;
    }

    @Override
    public void updateProductQuantity(List<OrderDetail> orderDetails) {
        List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).toList();
        List<ProductDTO> products = getProductByIds(productIds);
        Map<Long, ProductDTO> productMap = getProductMap(orderDetails, products);
        for(OrderDetail orderDetail: orderDetails) {
            ProductDTO currentProduct = productMap.get(orderDetail.getProductId());
            int existingQuantity = currentProduct.getAvailableQuantity();
            int updatedQuantity = existingQuantity - orderDetail.getQuantity();
            UpdateQuantityRequestDTO requestDTO = new UpdateQuantityRequestDTO();
            requestDTO.setQuantity(updatedQuantity);
            UpdateQuantityResponseDTO responseDTO = this.webClient
                                                        .patch()
                                                        .uri(BASE_URL + "/" + currentProduct.getId() + "/available_quantity")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .body(BodyInserters.fromValue(requestDTO))
                                                        .retrieve()
                                                        .bodyToMono(UpdateQuantityResponseDTO.class)
                                                        .block();
            System.out.println(responseDTO.getMessage());
        }
    }

}
