package dao.impl;

import dao.interfaces.OrderDao;
import model.Order;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrderDaoHibImpl implements OrderDao {

    private SessionFactory sessionFactory;
    private static final Logger logger = Logger.getLogger(OrderDaoHibImpl.class);

    @Autowired
    public OrderDaoHibImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Long> addOrder(Order order) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(order);
            logger.info(order + " added to db");
            return Optional.of(order.getId());
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return Optional.ofNullable(session.get(Order.class, id));
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
        return Optional.empty();
    }
}