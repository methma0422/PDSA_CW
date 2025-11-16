package lk.nibm.kd.hdse252.pdsa_cw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDTO {
    private String searchTerm;
    private String category;
    private String woodType;
    private Double minPrice;
    private Double maxPrice;
    private Integer minStock;
}


