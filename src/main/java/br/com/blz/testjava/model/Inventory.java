package br.com.blz.testjava.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Inventory {

    private Integer quantity;
    private Warehouse warehouse;
}
