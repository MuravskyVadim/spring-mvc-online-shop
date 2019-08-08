package service.impl;

import dao.interfaces.ProductDao;
import model.Product;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import service.interfaces.ProductService;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    @Autowired
    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    @Transactional
    public void addProduct(Product product) {
        productDao.addProduct(product);
    }

    @Override
    @Transactional
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Override
    @Transactional
    public Optional<Product> getProductById(Long id) {
        return productDao.getProductById(id);
    }

    @Override
    @Transactional
    public void removeProduct(Product product) {
        productDao.removeProduct(product);
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {
        productDao.updateProduct(product);
    }
}
