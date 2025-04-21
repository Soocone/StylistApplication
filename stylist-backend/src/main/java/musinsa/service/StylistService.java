package musinsa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import musinsa.dto.BrandDto;
import musinsa.dto.CategoryDto;
import musinsa.mapper.StylistDao;
import musinsa.dto.BrandCategoryDto;
import musinsa.util.CustomException;
import musinsa.util.InitDataLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service @Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class StylistService {

    private final StylistDao stylistDao;
    List<String>  categories = InitDataLoader.CATEGORIES;

        
    /**
      *  서비스명: getBestPriceByCategory
      *  설명: 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
      *  param: List<Brand>
      *  return: 조회 결과
    */
    public Map<String, Object> getBestPriceByCategory() {
        log.info("최저가격 브랜드 조회 시작");
        Map<String, Object> result = new LinkedHashMap<>();
        int total = 0;

            for (String category : categories) {
            List<Map<String, Object>> rows = stylistDao.getResultByCategory(category);

                if (rows.isEmpty()) {
                    throw new CustomException("데이터가 존재하지 않습니다: ", 404);
                } else {
                    Map<String, Object> row = rows.get(0);
                    result.put(category, row);
                    total = (int) row.get("PRICE");

                    log.info("카테고리 {} : 최저가 {}", category, row);
                }
            }
        log.info("카테고리별 최저가 조회 결과 총액: {}", total);

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
        log.info("최저가에 판매하는 단일 브랜드 조회 시작");

        // 모든 브랜드 카테고리별 상품 조회
        List<Map<String, Object>> brands = stylistDao.getDashboard();
        if(brands.isEmpty()) {
            throw new CustomException("데이터가 존재하지 않습니다: ", 404);
        }

        Map<String, Object> bestResult = new LinkedHashMap<>();
        int minTotal = Integer.MAX_VALUE;

        for (Map<String, Object> brand : brands) {
            String brandName = (String) brand.get("BRAND_NAME");

            // 카테고리별 가격 추출
            Map<String, Integer> categoryMap = new LinkedHashMap<>();
            for (String key : brand.keySet()) {
                if (!key.equals("BRAND_ID") && !key.equals("BRAND_NAME")) {
                    categoryMap.put(key, (Integer) brand.get(key));
                }
            }

            // 카테고리별 합계액
            int total = categories.stream()
            .mapToInt(cat -> categoryMap.getOrDefault(cat, Integer.MAX_VALUE))
            .sum();

            if (total < minTotal) {
            minTotal = total;

            // 카테고리 가공
            List<Map<String, String>> categoryList = categoryMap.entrySet().stream()
                .map(e -> {
                    Map<String, String> category = new LinkedHashMap<>();
                    category.put("카테고리", e.getKey());
                    category.put("가격", e.getValue().toString());
                    return category;
                })
                .collect(Collectors.toList());

            // return 세팅
            Map<String, Object> best = new LinkedHashMap<>();
            best.put("브랜드", brandName);
            best.put("카테고리", categoryList);
            best.put("총액", total);

            bestResult.put("최저가", best);
        }
    }
    return bestResult;
    }

        
    /**
      *  서비스명: getResultByCategory
      *  설명: 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
      *  param: String 조회 카테고리명
      *  return: 조회 결과
    */
    public Map<String, Object> getResultByCategory(CategoryDto categoryDto) {
        String categoryName = categoryDto.getName();
        // log.info("입력검색어==>"  categoryName);
                
        // 카테고리명으로 최저, 최고 가격 브랜드/가격 조회
        List<Map<String, Object>> rows = stylistDao.getResultByCategory(categoryName);
        Map<String, Object> result = new LinkedHashMap<>();
        
        //  조회 결과가 없는 경우 throw
        if (rows.isEmpty()) {
            throw new CustomException("해당 카테고리에 대한 데이터가 존재하지 않습니다: " +  categoryName, 404);
        } else {
            List<Map<String, Object>> maxList = new ArrayList<>();
            maxList.add(rows.get(0));
            List<Map<String, Object>> minList = new ArrayList<>();
            minList.add(rows.get(rows.size() - 1));

            // return 세팅
            result.put("카테고리", maxList.get(0).get("CATEGORY"));
            result.put("최저가", maxList);
            result.put("최고가", minList);

            log.info("{} 카테고리의 최저가: {}, 최고가: {}", maxList.get(0).get("CATEGORY"), rows.get(0), rows.get(rows.size() - 1));
        }

        return result;
    }

        
    /**
      *  서비스명: simulation
      *  설명: 코디 시뮬레이션
      *  param: List<Brand>
      *  return: 조회 결과
    */
    public Map<String, Object> simulation() {
        log.info("랜덤 코디 시뮬레이션 시작");

        Map<String, Object> result = new LinkedHashMap<>();
        int total = 0;

        for (String category : InitDataLoader.CATEGORIES) {
            // 전체 브랜드 조회
            List<Map<String, Object>> brandList = stylistDao.getBrandsByCategory(category);

            //  조회 결과가 없는 경우 throw
            if (brandList.isEmpty()) {
                throw new CustomException("데이터가 존재하지 않습니다." , 404);
            } else {
                // 랜덤 인덱스 생성
                int randomIndex = new Random().nextInt(brandList.size());

                // 랜덤 브랜드 가져오기
                Map<String, Object> selectedBrand = brandList.get(randomIndex);
                log.info(selectedBrand.toString());

                String brand = (String) selectedBrand.get("BRANDNAME");
                int price = Integer.parseInt(selectedBrand.get("PRICE").toString());

                // return 세팅
                Map<String, Object> item = new HashMap<>();
                item.put("brand", brand);
                item.put("price", price);
                result.put(category, item);

                total = price;
            }
        }

        result.put("total", total);
        return result;
    }

        
    /**
      *  서비스명: getTopCategoryByBrand
      *  설명: 각 브랜드별로 평균 가격이 가장 높은 카테고리와 그 평균 가격 조회
      *  param:
      *  return: 조회 결과
    */
    public List<Map<String, Object>> getTopCategoryByBrand() {
        log.info("각 브랜드별로 평균 가격이 가장 높은 카테고리와 그 평균 가격 조회 시작");
        return stylistDao.getTopCategoryByBrand();
    }

        
    /**
      *  서비스명: compareBrandPrice
      *  설명: 카테고리별 금액 비교
      *  param: Brand
      *  return: Map<String, Object>
    */
    public Map<String, Object> compareBrandPrice(BrandDto brandDto) {
        log.info("카테고리별 금액 비교 시작");

        // 선택된 brand_id 가져오기
        Long brandId = brandDto.getBrandId();

        // 선택한 브랜드의 카테고리별 가격 조회
        List<Map<String, Object>> brandPriceList = stylistDao.getBrandCategoryPrices(brandId);
        if (brandPriceList.isEmpty()) {
            throw new CustomException("해당 브랜드 가격 정보가 없습니다.", 404);
        }
        log.info(brandPriceList.toString());

        // 전체 브랜드 평균 가격 조회
        List<Map<String, Object>> avgPriceList = stylistDao.getAverageCategoryPrices();
        if (avgPriceList.isEmpty()) {
            throw new CustomException("전체 평균 가격 정보가 없습니다.", 404);
        }
        log.info(avgPriceList.toString());

        // 브랜드의 카테고리별 가격 담기
        Map<String, Integer> brandPrices = new HashMap<>();
        for (Map<String, Object> entry : brandPriceList) {
            String category = (String) entry.get("CATEGORY");
            Object priceObj = entry.get("PRICE");

            if (category != null && priceObj instanceof Number) {
                brandPrices.put(category, ((Number) priceObj).intValue());
            }
        }

        // 카테고리별 평균 가격 담기
        Map<String, Double> avgPrices = new HashMap<>();
        for (Map<String, Object> entry : avgPriceList) {
            String category = (String) entry.get("CATEGORY");
            Object avgObj = entry.get("PRICE");

            if (category != null && avgObj instanceof Number) {
                avgPrices.put(category, ((Number) avgObj).doubleValue());
            }
        }

        // 비교 결과 생성 (전체 카테고리를 기준으로)
        Map<String, Map<String, Object>> comparison = new LinkedHashMap<>();
        for (String category : InitDataLoader.CATEGORIES) {
            Integer brandPrice = brandPrices.get(category);
            Double avgPrice = avgPrices.get(category);

            // 가격 정보 담기
            Map<String, Object> comparisonInfo = new LinkedHashMap<>();
            comparisonInfo.put("brandPrice", brandPrice != null ? brandPrice : "N/A");
            comparisonInfo.put("averagePrice", avgPrice != null ? avgPrice.intValue() : "N/A");

            if (brandPrice == null || avgPrice == null || avgPrice == 0.0) {
                comparisonInfo.put("diffPercent", "N/A");
            } else {
                // 평균과의 가격 비교 차이 담기
                double diffPercent = ((brandPrice - avgPrice) / avgPrice) * 100;
                String formatted = String.format("%.1f%%", diffPercent);
                comparisonInfo.put("diffPercent", formatted);
            }
                comparison.put(category, comparisonInfo);
        }

        // return 세팅
        Map<String, Object> result = new HashMap<>();
        result.put("brandName", stylistDao.getBrandNameById(brandId));
        result.put("priceComparison", comparison);
        return result;
    }

        
    /**
      *  서비스명: getAll
      *  설명: 대시보드용 조회
      *  return: 조회 결과
    */
    public List<Map<String, Object>> getAll() {
        log.info("대시보드용 전체 브랜드, 상품 조회 시작");

        // 대시보드용 브랜드 리스트 조회
        List<Map<String, Object>> brandList = stylistDao.getDashboard();
        //  조회 결과가 없는 경우 throw
        if (brandList.isEmpty()) {
            throw new CustomException("데이터가 존재하지 않습니다." , 404);
        }

        List<Map<String, Object>> result = new ArrayList<>();

        // 프론트 인터페이스 방식에 맞게 리턴 결과 Map으로 반환
        for (Map<String, Object> item : brandList) {

            // 브랜드 정보 담기
            Map<String, Object> brandMap = new LinkedHashMap<>();
            brandMap.put("brandId", item.get("BRAND_ID"));
            brandMap.put("brandName", item.get("BRAND_NAME"));

            Map<String, Object> categoryMap = new LinkedHashMap<>();

            for (Map.Entry<String, Object> entry : item.entrySet()) {
            String key = entry.getKey();
            // 카테고리 정보 담기
                    if (!key.equals("BRAND_ID") && !key.equals("BRAND_NAME")) {
                    categoryMap.put(key, entry.getValue());
                }
            }

            // return 세팅
            brandMap.put("categories", categoryMap);
            result.add(brandMap);
        }

        return result;
    }

    /**
      *  서비스명: inqAllInfo
      *  설명: 브랜드, 상품 전체 조회
      *  return: 조회 결과
    */
    public List<Map<String, Object>> inqAllInfo() {
        log.info("전체 브랜드, 상품 조회 시작");
        return stylistDao.getAllInfo();
    }

        
    /**
      *  서비스명: getAllBrands
      *  설명: 브랜드 전체 조회
      *  return: 조회 결과
    */
    public List<Map<String, Object>> getAllBrands() {
        log.info("브랜드 전체 조회 시작");
        return stylistDao.getAllBrands();
    }

        
    /**
      *  서비스명: insertBrand
      *  설명: 단건 브랜드 등록
      *  param: BrandDto
      *  return: 수행 결과(boolean)
    */
    public void insertBrand(BrandDto brandDto) {
        log.info("등록 브랜드: {}", brandDto);

        // 중복 브랜드 조회
        int existing = stylistDao.findBrandByName(brandDto.getName());

        // 중복 브랜드 존재 시
        if (existing > 0) {
            throw new CustomException("중복 데이터가 존재합니다." , 500);
        } else {
            // 브랜드 및 상품 등록
            try {
                stylistDao.insertBrand(brandDto);
            } catch (Exception e) {
                throw new CustomException("브랜드 등록 중 에러가 발생했습니다." , 500);
            }

            // 등록 시 채번된 brand_id 카테고리 입력용으로 세팅
            for(BrandCategoryDto category : brandDto.getCategories()) {
                category.setBrandId(brandDto.getBrandId());
            }

            // 카테고리별 금액 등록
            try {
                stylistDao.insertBrandCategories(brandDto.getCategories());
            } catch (Exception e) {
                throw new CustomException("브랜드 카테고리별 금액 등록 중 에러가 발생했습니다." , 500);
            }
        }
    }

        
    /**
      *  서비스명: updateBrand
      *  설명: 단건 브랜드 수정
      *  param: Brand
      *  return: 수행 결과(boolean)
    */
    public void updateBrand(BrandDto brandDto) {
        log.info("수정 브랜드: {}", brandDto);

        // 브랜드 및 상품 수정
        stylistDao.updateBrand(brandDto);

        for(BrandCategoryDto category : brandDto.getCategories()) {
            category.setBrandId(brandDto.getBrandId());
            stylistDao.updateBrandCategories(category);
        }
    }

        
    /**
      * 서비스명: deleteBrand
      * 설명: 브랜드 다건 삭제
      * param: Brand
      * return:
    */
    public void deleteBrand(List<Long> brandIds) {
        log.info("삭제 브랜드: {}", brandIds);

        // 브랜드, 브랜드 카테고리 삭제
        stylistDao.deleteBrandCategories(brandIds);
        stylistDao.deleteBrandById(brandIds);
    }

    /**
      *  서비스명: deleteAll
      *  설명: 브랜드 전체 삭제
      *  return:
    */
    public void deleteAll() {
        stylistDao.deleteAll();
    }
}
