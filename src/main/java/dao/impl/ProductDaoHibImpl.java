package dao.impl;

import dao.interfaces.ProductDao;
import model.Product;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductDaoHibImpl implements ProductDao {

    private static final Logger logger = Logger.getLogger(ProductDaoHibImpl.class);

    private SessionFactory sessionFactory;

    @Autowired
    public ProductDaoHibImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addProduct(Product product) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(product);
            logger.info(product + " added to db");
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM Product").list();
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return Optional.ofNullable(session.get(Product.class, id));
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
        return Optional.empty();
    }

    @Override
    public void removeProduct(Product product) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.delete(product);
            logger.info(product + " deleted from db");
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }

    @Override
    public void updateProduct(Product product) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.update(product);
            logger.info(product + " updated in db");
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }
}