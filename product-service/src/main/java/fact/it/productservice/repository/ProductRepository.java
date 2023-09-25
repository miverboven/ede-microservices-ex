package fact.it.productservice.repository;

import fact.it.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findBySkuCodeIn(List<String> skuCode);
}
