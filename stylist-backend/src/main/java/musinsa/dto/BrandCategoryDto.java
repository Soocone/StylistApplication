package musinsa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandCategoryDto {
    private Long brandId; // pk
    private String category;
    private Long price;
}
