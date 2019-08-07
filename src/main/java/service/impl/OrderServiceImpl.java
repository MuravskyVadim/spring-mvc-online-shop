package service.impl;

import dao.interfaces.OrderDao;
import model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.interfaces.OrderService;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    @Transactional
    public Optional<Long> addOrder(Order order) {
        return orderDao.addOrder(order);
    }

    @Override
    @Transactional
    public Optional<Order> getOrderById(Long id) {
        return orderDao.getOrderById(id);
    }
}
