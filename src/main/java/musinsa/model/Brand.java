package musinsa.model;

import lombok.Data;

import java.util.Map;

@Data
public class Brand {
    private Long id; // pk
    private String name; // 브랜드명
    private Map<String, Integer> categories; // 카테고리명, 금액으로 구성
}
