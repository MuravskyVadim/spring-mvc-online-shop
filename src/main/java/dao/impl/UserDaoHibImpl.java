package dao.impl;

import dao.interfaces.UserDao;
import model.User;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoHibImpl implements UserDao {

    private static final Logger logger = Logger.getLogger(UserDaoHibImpl.class);

    private SessionFactory sessionFactory;

    @Autowired
    public UserDaoHibImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addUser(User user) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(user);
            logger.info(user + " added to db");
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.update(user);
            logger.info(user + " updated in db");
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }

    @Override
    public void removeUser(User user) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.delete(user);
            logger.info(user + " deleted from db");
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM User").list();
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return Optional.ofNullable(session.get(User.class, id));
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        try {
            Session session = sessionFactory.getCurrentSession();
            User user = (User) session.createQuery("FROM User WHERE email = :email")
                    .setParameter("email", email)
                    .list()
                    .get(0);
            return Optional.of(user);
        } catch (Exception e) {
            logger.error("Exception: " + e);
        }
        return Optional.empty();
    }
}