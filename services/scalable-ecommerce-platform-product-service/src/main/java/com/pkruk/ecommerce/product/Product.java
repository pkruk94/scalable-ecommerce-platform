package com.pkruk.ecommerce.product;

import com.pkruk.ecommerce.product_category.ProductCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence"
    )
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int availableQuantity;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;
}
