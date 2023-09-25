package fact.it.productservice.service;

import fact.it.productservice.dto.ProductRequest;
import fact.it.productservice.dto.ProductResponse;
import fact.it.productservice.model.Product;
import fact.it.productservice.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @PostConstruct
    public void loadData() {
        if(productRepository.count() <= 0){
            Product product = Product.builder()
                    .skuCode("tube6in")
                    .name("Stainless steel acid-resistant seamless tube OD 6 mm x 1 mm steel grade 316 / 316L 180cm")
                    .price(BigDecimal.valueOf(7.50))
                    .description("")
                    .build();

            Product product1 = Product.builder()
                    .skuCode("beam10ft")
                    .name("Wooden beam 10 feet long")
                    .price(BigDecimal.valueOf(5.50))
                    .description("Faux Wood Beams - 10 ft. Length & 10 in. Width Cast from natural wood beams with surface textures and wood-grain detail")
                    .build();

            productRepository.save(product);
            productRepository.save(product1);
        }
    }

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .skuCode(productRequest.getSkuCode())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    public List<ProductResponse> getAllProductsBySkuCode(List<String> skuCode) {
        List<Product> products = productRepository.findBySkuCodeIn(skuCode);

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .skuCode(product.getSkuCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

}
