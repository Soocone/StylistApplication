package musinsa.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter @AllArgsConstructor
public class Brand {
    private String name; // 브랜드명
    private Map<String, Integer> categories; // 카테고리명, 금액으로 구성된 Map
}
