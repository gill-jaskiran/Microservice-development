package ca.gbc.productservice.controller;
//Controller
import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.service.ProductService;
import io.micrometer.core.ipc.http.HttpSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

  /*  public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest); -- Would not work in integration testing as it returns void
    }*/

    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        ProductResponse createdProduct = productService.createProduct(productRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/product/" + createdProduct.id());

        return ResponseEntity
                .status(HttpStatus.CREATED) // Sets status to 201
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdProduct);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }


    //https://localhost:8080/api/product
    @PutMapping("/{productId}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> updateProduct(@PathVariable("productId") String productId,
                                           @RequestBody ProductRequest productRequest){


        String updatedProductId = productService.updateProduct(productId, productRequest);

        // set the location header attribute
        HttpHeaders headers =new HttpHeaders();
        headers.add("Location", "/api/product/" + updatedProductId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") String productId) {

        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}

