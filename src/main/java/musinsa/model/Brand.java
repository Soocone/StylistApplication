package musinsa.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Brand {
    private Long id; // pk
    private String name; // 브랜드명
    private List<Brand_Category> categories; // 카테고리명, 금액으로 구성
}
