package com.auction.portal.product.controller;

import com.auction.portal.product.dto.ProductDto;
import com.auction.portal.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws FileNotFoundException {

        Resource resource = productService.loadFile(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/upload")
    public ProductDto uploadImage(@RequestParam("id") int productId, @RequestParam("file") MultipartFile file) throws IOException {
        return productService.uploadImage(productId,file);
    }

    @GetMapping("/{category}")
    public List<ProductDto> getProductsByCategory(@PathVariable("category") String category){
        return productService.getProductsByCategory(category);
    }
}
