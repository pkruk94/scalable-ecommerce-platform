package com.pkruk.ecommerce.product_category;

import com.pkruk.ecommerce.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_category_sequence")
    @SequenceGenerator(
            name = "product_category_sequence",
            sequenceName = "product_category_sequence"
    )
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "productCategory")
    private List<Product> products;
}
