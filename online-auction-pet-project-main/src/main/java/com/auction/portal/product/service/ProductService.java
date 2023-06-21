package com.auction.portal.product.service;

import com.auction.portal.product.dto.ProductDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ProductService {

    @PostConstruct
    void init();

    List<ProductDto> getAllProducts();

    ProductDto getProductById(int id);

    ProductDto saveProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    String deleteProductById(int id);

    ProductDto uploadImage(int productId, MultipartFile multipartFile) throws IOException;

    Resource loadFile(String fileName) throws FileNotFoundException;

    List<ProductDto> getProductsByCategory(String category);
}
