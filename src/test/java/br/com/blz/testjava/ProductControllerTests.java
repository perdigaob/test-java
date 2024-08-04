package br.com.blz.testjava;

import br.com.blz.testjava.controller.ProductController;
import br.com.blz.testjava.exception.ProductExistsException;
import br.com.blz.testjava.exception.ProductNotFoundException;
import br.com.blz.testjava.model.Inventory;
import br.com.blz.testjava.model.Product;
import br.com.blz.testjava.model.Warehouse;
import br.com.blz.testjava.model.WarehouseType;
import br.com.blz.testjava.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductControllerTests {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = buildProduct(50001L, "Test Product 001", 5, true,
            Arrays.asList(
                new Warehouse("SP", 2, WarehouseType.ECOMMERCE),
                new Warehouse("MOEMA", 3, WarehouseType.PHYSICAL_STORE)
            ));
    }

    @Test
    void test_ShouldReturnProduct_WhenGetProduct() throws ProductNotFoundException {
        when(productService.getProductBySku(50001L)).thenReturn(product);

        ResponseEntity<Product> response = productController.getProduct(50001L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Test Product 001", response.getBody().getName());
        Assertions.assertEquals(5, response.getBody().getInventory().getQuantity());
        Assertions.assertEquals(true, response.getBody().getIsMarketable());
    }

    @Test
    void test_ShouldCreateProductSuccessfully() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        ResponseEntity<Product> response = productController.createProduct(product);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(product, response.getBody());
    }

    @Test
    void test_ShouldUpdateProductSuccessfully() throws ProductNotFoundException {
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(product);

        ResponseEntity<Product> response = productController.updateProduct(50001L, product);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(product, response.getBody());
    }

    @Test
    void test_ShouldDeleteProductSuccessfully() throws ProductNotFoundException {
        doNothing().when(productService).deleteProductBySku(50001L);

        ResponseEntity<Void> response = productController.deleteProduct(50001L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void test_ShouldFailToDeleteProduct_WhenProductDoesNotExist() throws ProductNotFoundException {
        doThrow(new ProductNotFoundException("Product not found.")).when(productService).deleteProductBySku(50002L);

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productController.deleteProduct(50002L);
        });
    }

    @Test
    void test_ShouldFailToGetProduct_WhenProductDoesNotExist() throws ProductNotFoundException {
        when(productService.getProductBySku(50002L)).thenThrow(new ProductNotFoundException("Product not found."));

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productController.getProduct(50002L);
        });
    }

    @Test
    void test_ShouldFailToUpdateProduct_WhenProductDoesNotExist() throws ProductNotFoundException {
        when(productService.updateProduct(anyLong(), any(Product.class))).thenThrow(new ProductNotFoundException("Product not found."));

        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productController.updateProduct(50001L, product);
        });
    }

    @Test
    void test_ShouldFailToCreateProduct_WhenProductAlreadyExists() throws Exception {
        when(productService.createProduct(any(Product.class))).thenThrow(new ProductExistsException("Product already exists."));

        Assertions.assertThrows(ProductExistsException.class, () -> {
            productController.createProduct(product);
        });
    }

    @Test
    void test_ShouldFailToUpdateProduct_WhenSkusDoNotMatch() {
        Assertions.assertThrows(InvalidParameterException.class, () -> {
            productController.updateProduct(50002L, product);
        });
    }

    @Test
    void test_ShouldSetIsMarketableToFalse_WhenQuantityIsZero() throws ProductNotFoundException {
        Product productWithZeroQuantity = buildProduct(50003L, "Test Product 003", 0, false,
            Arrays.asList(
                new Warehouse("BA", 0, WarehouseType.ECOMMERCE),
                new Warehouse("Salvador", 0, WarehouseType.PHYSICAL_STORE)
            ));

        when(productService.getProductBySku(50003L)).thenReturn(productWithZeroQuantity);

        ResponseEntity<Product> response = productController.getProduct(50003L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(0, response.getBody().getInventory().getQuantity());
        Assertions.assertFalse(response.getBody().getIsMarketable());
    }

    private Product buildProduct(Long sku, String name, Integer quantity, Boolean isMarketable, List<Warehouse> warehouses) {
        Product product = new Product();
        product.setSku(sku);
        product.setName(name);
        product.setIsMarketable(isMarketable);

        Inventory inventory = new Inventory();
        inventory.setQuantity(quantity);
        inventory.setWarehouses(warehouses);
        product.setInventory(inventory);

        return product;
    }
}
