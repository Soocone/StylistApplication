package musinsa.dto;

import lombok.Data;
import java.util.List;

@Data
public class BrandDto {
    private Long brandId; // pk
    private String name; // 브랜드명
    private List<BrandCategoryDto> categories; // 카테고리명, 금액으로 구성
}
