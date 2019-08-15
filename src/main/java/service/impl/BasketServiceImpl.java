package service.impl;

import dao.interfaces.BasketDao;
import model.Basket;
import model.Product;
import model.User;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import service.interfaces.BasketService;

import java.util.List;
import java.util.Optional;

@Service
public class BasketServiceImpl implements BasketService {

    private BasketDao basketDao;

    @Autowired
    public BasketServiceImpl(BasketDao basketDao) {
        this.basketDao = basketDao;
    }

    @Override
    @Transactional
    public void addProduct(Product product, User user) {
        basketDao.addProduct(product, user);
    }

    @Override
    @Transactional
    public List<Product> getAllProducts(User user) {
        return basketDao.getAllProducts(user);
    }

    @Override
    @Transactional
    public Optional<Product> getProductById(Long id) {
        return basketDao.getProductById(id);
    }

    @Override
    @Transactional
    public void removeProduct(Product product, User user) {
        basketDao.removeProduct(product, user);
    }

    @Override
    @Transactional
    public void clear(User user) {
        basketDao.clearBasket(user);
    }

    @Override
    @Transactional
    public Optional<Basket> getBasketById(Long id) {
        return basketDao.getBasketById(id);
    }
}
