package com.stockwage.commercial.sales.dto;
import lombok.Data;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Double salePrice;
    private Integer categoryId;
}
