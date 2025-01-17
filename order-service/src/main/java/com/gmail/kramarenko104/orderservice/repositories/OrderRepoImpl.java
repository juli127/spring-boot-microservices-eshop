package com.gmail.kramarenko104.orderservice.repositories;

import com.gmail.kramarenko104.orderservice.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderRepoImpl extends BaseRepoImpl<Order> implements OrderRepo {

    private final static Logger logger = LoggerFactory.getLogger(OrderRepoImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        Order newOrder = null;
        order.setUser(em.merge(order.getUser()));
        em.persist(order);
        long orderId = order.getOrder_id();
        newOrder = em.find(Order.class, orderId);
        logger.debug("[eshop] OrderRepoImpl.createOrderForUser:...new Order was created: " + newOrder);
        return newOrder;
    }

    @Override
    public List<Order> getAllOrdersByUserId(long userId) {
        TypedQuery<Order> query = em.createNamedQuery("GET_ALL_ORDERS_BY_USERID", Order.class)
                .setParameter("userId", userId);
        List<Order> resultList = query.getResultList();
        logger.debug("[eshop] OrderRepoImpl.getAllOrdersForUser: List of all orders is: " + resultList.toString());
        return resultList;
    }

    @Override
    public Order getLastOrderByUserId(long userId) {
        Order order = null;
        logger.debug("[eshop] OrderRepoImpl.getLastOrderByUserId: get order for userId: " + userId);
        try {
            TypedQuery<Order> query = em.createNamedQuery("GET_LAST_ORDER_BY_USERID", Order.class)
                    .setParameter("userId", userId);
            order = query.setMaxResults(1).getSingleResult();
            logger.debug("[eshop] OrderRepoImpl.getLastOrderByUserId: the last orders is: " + order);
        } catch (NoResultException ex) {
            logger.debug("[eshop] OrderRepoImpl.getLastOrderByUserId: Order was not found in DB for userId=" + userId);
        }
        return order;
    }

    public long getNewOrderNumber() {
        // generate order number (orderNumber is not auto-increment in 'orders' table)
        // because of one order can have many products ==> many rows can have the same orderNumber in 'orders' table
        Object result = em.createNamedQuery("GET_LAST_ORDER_NUMBER").getSingleResult();
        long lastOrderNumber = (result == null ? 0L : (long) result);
        return ++lastOrderNumber;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = Exception.class)
    public void deleteAllOrdersForUser(long userId) {
        TypedQuery<Order> query = em.createNamedQuery("GET_ALL_ORDERS_BY_USERID", Order.class)
                .setParameter("userId", userId);
        List<Order> ordersToRemove = query.getResultList();
        ordersToRemove.stream().forEach(order -> em.remove(order));
    }
}