package br.com.blz.testjava.exception;

public class ProductExistsException extends Exception {
    public ProductExistsException(String message) {
        super(message);
    }
}
