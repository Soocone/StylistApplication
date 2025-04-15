package musinsa.stylist;

import musinsa.model.Brand;
import musinsa.util.InitialData;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StylistService {

    private final List<Brand> brands = InitialData.generate();

    // 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
    public Map<String, Object> getBestPriceByCategory() {

        Map<String, Map<String, Object>> result = new HashMap<>();
        for(String category : InitialData.categoryNames) {
            Brand best = brands.stream()
                    .min(Comparator.comparingInt(c -> c.getCategories().get(category)))
                    .orElseThrow();

            Map<String, Object> info = new HashMap<>();
            info.put("brand", best.getName());
            info.put("price", best.getCategories().get(category));

            result.put(category, info);
        }

        int total = result.values().stream()
                .mapToInt(res -> (int) res.get("price"))
                .sum();

        return Map.of("result", result, "total", total);
    }
}
