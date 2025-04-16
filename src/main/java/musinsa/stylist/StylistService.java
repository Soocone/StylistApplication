package musinsa.stylist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import musinsa.dao.StylishDao;
import musinsa.model.Brand;
import musinsa.util.InitialData;
import org.springframework.stereotype.Service;

import java.util.*;

@Service @Slf4j
@RequiredArgsConstructor
public class StylistService {

    private final StylishDao stylishDao;
    List<String>  categories = InitDataLoader.CATEGORIES;

    public void modifyBrands(List<Brand> brands) {
        log.info("업데이트 브랜드: {}", brands);

        for (Brand b : brands) {
            modifyBrand(b);
        }
    }


    // 브랜드 및 상품을 추가 / 업데이트 / 삭제
    public void modifyBrand(Brand brand) {
        log.info("업데이트 브랜드: {}", brand);

        stylishDao.insertBrand(brand);
        for (Map.Entry<String, Integer> entry : brand.getCategories().entrySet()) {
            log.debug("업데이트 브랜드 카테고리 {} - 가격 {}", entry.getKey(), entry.getValue());

            stylishDao.insertBrandCategory(brand.getId(), entry.getKey(), entry.getValue());
        }
    }

    // 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
    public Map<String, Object> getResultByCategory() {
        Map<String, Object> result = new LinkedHashMap<>();
        int total = 0;

        for (String category : categories) {
            List<Map<String, Object>> rows = stylishDao.getResultByCategory(category);

            if (!rows.isEmpty()) {
                Map<String, Object> row = rows.get(0);
                result.put(category, row);
                total += (int) row.get("price");

                log.debug("카테고리 {} : 최저가 {}", category, row);
            }
        }
        log.debug("카테고리별 최저가 조회 결과 총액: {}", total);

        result.put("total", total);
        return result;
    }


    // 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회
    public Map<String, Object> getBestPriceFromSingleBrand() {
        List<Brand> brands = stylishDao.getAllBrands();
        Map<String, Object> best = new HashMap<>();
        int minTotal = Integer.MAX_VALUE;
        for (Brand b : brands) {
            Map<String, Integer> category = b.getCategories();
            if (category.keySet().containsAll(categories)) {
                int total = categories.stream().mapToInt(p -> category.getOrDefault(p, Integer.MAX_VALUE)).sum();
                if (total < minTotal) {
                    minTotal = total;
                    best = new LinkedHashMap<>();
                    best.put("brand", b.getName());
                    best.put("category", category);
                    best.put("total", total);

                    log.debug("최저가격에 판매하는 단일 브랜드: {}, 총액: {}", b.getName(), total);
                }
            }
        }
        return best;
    }


    // 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
    public Map<String, Object> getResultByCategory(String category) {
        List<Map<String, Object>> rows = stylishDao.getResultByCategory(category);
        Map<String, Object> result = new LinkedHashMap<>();
        if (!rows.isEmpty()) {
            result.put("min", rows.get(0));
            result.put("max", rows.get(rows.size() - 1));

            log.debug("{} 카테고리의 최저가: {}, 최고가: {}", category, rows.get(0), rows.get(rows.size() - 1));
        }
        return result;
    }

    public List<Brand> getAll() {
        return stylishDao.getAllBrands();
    }

    public void clearAll() {
        log.info("브랜드 전부 삭제 처리 시작");
        stylishDao.deleteAll();
    }

//    private List<Brand> brands = InitialData.generate();
//
//    // 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
//    public Map<String, Object> getBestPriceByCategory() {
//
//        Map<String, Map<String, Object>> result = new HashMap<>();
//        for(String category : InitialData.categoryNames) {
//            Brand best = brands.stream()
//                    .min(Comparator.comparingInt(c -> c.getCategories().get(category)))
//                    .orElseThrow();
//
//            Map<String, Object> info = new HashMap<>();
//            info.put("brand", best.getName());
//            info.put("price", best.getCategories().get(category));
//
//            result.put(category, info);
//        }
//
//        int total = result.values().stream()
//                .mapToInt(res -> (int) res.get("price"))
//                .sum();
//
//        log.debug("부품별 최저가 조회 결과 총액: {}", total);
//
//        return Map.of("result", result, "total", total);
//    }
//
//
//    // 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회
//    public Map<String, Object> getBestBrandByPrice() {
//
//        Brand best = brands.stream()
//                .min(Comparator.comparingInt(c -> c.getCategories().values().stream().mapToInt(Integer::intValue).sum()))
//                .orElseThrow();
//
//        int total = best.getCategories().values().stream().mapToInt(i ->i).sum();
//
//        log.debug("최저가격에 판매하는 단일 브랜드: {}, 총액: {}", best.getName(), total);
//
//        return Map.of("brand", best.getName()
//                , "category", best.getCategories()
//                , "total", total);
//
//    }
//
//    // 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
//    public Map<String, Object> getPriceRange(String categoryName) {
//
//        Brand minBrand = brands.stream()
//                .min(Comparator.comparingInt(c-> c.getCategories().get(categoryName)))
//                .orElseThrow();
//
//        Brand maxBrand = brands.stream()
//                .max(Comparator.comparingInt(c-> c.getCategories().get(categoryName)))
//                .orElseThrow();
//
//        Map<String, Object> finalMin = new HashMap<>();
//        finalMin.put("브랜드", minBrand.getName());
//        finalMin.put("가격", minBrand.getCategories().get(categoryName));
//
//        Map<String, Object> finalMax = new HashMap<>();
//        finalMax.put("브랜드", maxBrand.getName());
//        finalMax.put("가격", maxBrand.getCategories().get(categoryName));
//
//        log.info("{} 카테고리의 최저가, 최고가 조회- 최저가: {}, 최고가: {}", categoryName, finalMin, finalMax);
//        //최저가 최고가는 배열로 구성돼야 함...... 수정 필요
//        return Map.of(
//                "카테고리", categoryName
//                , "최저가", finalMin
//                , "최고가", finalMax
//        );
//    }
//
//    // 브랜드 및 상품을 추가 / 업데이트 / 삭제
//    public void modifyBrands(List<Brand> newBrands) {
//        log.info("브랜드 목록 업데이트 요청 수신 - 총 {}개", newBrands.size());
//        this.brands = newBrands;
//    }
}
