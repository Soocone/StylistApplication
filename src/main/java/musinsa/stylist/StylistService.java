package musinsa.stylist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import musinsa.dao.StylishDao;
import musinsa.model.Brand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service @Slf4j @Transactional
@RequiredArgsConstructor
public class StylistService {

    private final StylishDao stylishDao;
    List<String>  categories = InitDataLoader.CATEGORIES;

    String msg = "";

    /**
     *  서비스명: modifyBrands
     *  설명: 브랜드 등록/수정/삭제
     *  param: List<Brand>
     *  return: 결과 메시지
     */
    public String modifyBrands(String flag, List<Brand> brands) {
        log.info("업데이트/삭제 브랜드 목록: {}", brands);

        int cnt = 0;
        switch (flag) {
            case "수정" :
                // 브랜드 1건씩 등록/수정 수행
                for (Brand brand : brands) {
                    cnt = modifyBrand(brand) ? cnt++ : 0;
                }
                break;

            case "삭제" :
                // 브랜드 1건씩 삭제 수행
                for (Brand brand : brands) {
                    cnt = deleteBrand(brand) ? cnt++ : 0;
                }
                break;
        }

        return "브랜드 " + cnt + " 건 "+ flag + "완료되었습니다.";
    }

    /**
     *  서비스명: modifyBrand
     *  설명: 단건 브랜드 등록/수정
     *       브랜드 및 상품을 추가 / 업데이트 - merge를 사용하려다가 중복데이터가 있는 경우 사용자에게 알려주고 전체롤백 시키기 위해
     *       기존 데이터 delete 후 insert하기보다, insert / update로 구현
     *  param: Brand
     *  return: 수행 결과(boolean)
     */
    public boolean modifyBrand(Brand brand) {
        log.info("업데이트 브랜드: {}", brand);

        // 중복 브랜드 조회
        int existing = stylishDao.findBrandByName(brand.getName());

        // 중복 브랜드 존재 시
        if (existing > 0) {
            msg = brand.getName() + " 브랜드는 이미 존재합니다.";
            return false;
        } else {
            // 브랜드 등록
            stylishDao.insertBrand(brand);

            // 브랜드 카테고리 등록
            for (Map.Entry<String, Integer> entry : brand.getCategories().entrySet()) {
                log.debug("업데이트 브랜드 카테고리 {} - 가격 {}", entry.getKey(), entry.getValue());
                stylishDao.insertBrandCategory(brand.getId(), entry.getKey(), entry.getValue());
            }
        }

        return true;
    }


    /**
     *  서비스명: deleteBrand
     *  설명: 단건 브랜드 삭제
     *  param: Brand
     *  return:
     */
    public Boolean deleteBrand(Brand brand) {
        log.info("삭제 브랜드: {}", brand);

        stylishDao.deleteBrand(brand);
        return true;
    }

    /**
     *  서비스명: getResultByCategory
     *  설명: 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
     *  param: List<Brand>
     *  return: 조회 결과
     */
    public Map<String, Object> getResultByCategory() {
        log.info("최저가격 브랜드 조회 시작");
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


    /**
     *  서비스명: getBestPriceFromSingleBrand
     *  설명: 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회
     *  param:
     *  return: 조회 결과
     */
    public Map<String, Object> getBestPriceFromSingleBrand() {
        // 모든 브랜드 조회
        List<Brand> brands = stylishDao.getAllBrands();
        Map<String, Object> best = new HashMap<>();
        // 총액 초기값은 최대값으로 설정
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


    /**
     *  서비스명: getResultByCategory
     *  설명: 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
     *  param: String 조회 카테고리명
     *  return: 조회 결과
     */
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

    /**
     *  서비스명: getAll
     *  설명: 브랜드 전체 조회
     *  param:
     *  return: 조회 결과
     */
    public List<Brand> getAll() {
        return stylishDao.getAllBrands();
    }


    /**
     * (폐기)DB 구성하지 않을 경우 로직
     */
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
