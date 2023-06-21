package com.auction.portal.product.service;

import com.auction.portal.config.properties.ImageUploadProperties;
import com.auction.portal.product.dto.ProductDto;
import com.auction.portal.product.entity.Product;
import com.auction.portal.product.exception.ImageUploadException;
import com.auction.portal.product.repository.ProductRepository;
import com.auction.portal.utils.ErrorCodes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    private final Path dirLocation;

    @Autowired
    public ProductServiceImpl(ImageUploadProperties imageUploadProperties) {
        this.dirLocation = Paths.get(imageUploadProperties.getLocation())
                .toAbsolutePath()
                .normalize();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(this.dirLocation);
        } catch (Exception ex) {
            throw new ImageUploadException(ErrorCodes.IMAGE_UPLOAD_ERROR);
        }
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCodes.PRODUCT_NOT_FOUND));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);

    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        productRepository.findById(productDto.getId()).
                orElseThrow(() -> new IllegalArgumentException(ErrorCodes.PRODUCT_NOT_FOUND));
        Product product = modelMapper.map(productDto, Product.class);
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public String deleteProductById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCodes.PRODUCT_NOT_FOUND));
        productRepository.saveAndFlush(product);
        return "Product with ID : " + id + " has been Deleted Successfully";
    }

    @Override
    public ProductDto uploadImage(int productId, MultipartFile multipartFile) throws IOException {
        ProductDto savedProduct = this.getProductById(productId);
        try {
            String fileName = multipartFile.getOriginalFilename();
            Path file = this.dirLocation.resolve(fileName);
            Files.copy(multipartFile.getInputStream(), file, StandardCopyOption.REPLACE_EXISTING);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/download/")
                    .path(fileName)
                    .toUriString();
            savedProduct.setProductImageUri(fileDownloadUri);
        }catch (ImageUploadException e){
            throw new ImageUploadException();
        }

        return this.updateProduct(savedProduct);

    }

    @Override
    public Resource loadFile(String fileName) throws FileNotFoundException {
        try {
            Path file = this.dirLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Could not find file");
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not download file");
        }

    }
    @Override
    public List<ProductDto> getProductsByCategory(String category){
        List<Product> productsByCategory = productRepository.findByCategory(category);
        return productsByCategory.stream().map(product -> modelMapper.map(product,ProductDto.class)).collect(Collectors.toList());
    }
}
