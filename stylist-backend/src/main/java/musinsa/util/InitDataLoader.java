package musinsa.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import musinsa.mapper.StylistDao;
import musinsa.dto.BrandDto;
import musinsa.dto.CategoryDto;
import musinsa.dto.BrandCategoryDto;
import org.springframework.stereotype.Component;

import java.util.*;

/**
  * 초기 데이터 적재
*/
@Slf4j
@Component
@RequiredArgsConstructor
public class InitDataLoader {

    private final StylistDao stylistDao;

    public static final List<String> CATEGORIES = Arrays.asList("상의", "아우터", "바지", "스니커즈", "가방", "모자", "양말", "액세서리");

    // 브랜드, 카테고리 별 금액 데이터 생성
    @PostConstruct
    public void init() {
        
        stylistDao.createBrandTable();
        stylistDao.createBrandCategoryTable();
        stylistDao.createCategoryTable();
        log.info("InitDataLoader Table Creation 완료");
        
        long[][] prices = {
            {11200, 5500, 4200, 9000, 2000, 1700, 1800, 2300}, // A
            {10500, 5900, 3800, 9100, 2100, 2000, 2000, 2200}, // B
            {10000, 6200, 3300, 9200, 2200, 1900, 2200, 2100}, // C
            {10100, 5100, 3000, 9500, 2500, 1500, 2400, 2000}, // D
            {10700, 5000, 3800, 9900, 2300, 1800, 2100, 2100}, // E
            {11200, 7200, 4000, 9300, 2100, 1600, 2300, 1900}, // F
            {10500, 5800, 3900, 9000, 2200, 1700, 2100, 2000}, // G
            {10800, 6300, 3100, 9700, 2100, 1600, 2000, 2000}, // H
            {11400, 6700, 3200, 9500, 2400, 1700, 1700, 2400}  // I
        };

        // 카테고리 테이블 적재
        try{
            for(int i = 0; i < CATEGORIES.size(); i++) {
                stylistDao.insertCategories(new CategoryDto((long)i, CATEGORIES.get(i)));
            }
        } catch (Exception e) {
        log.info("InitDataLoader 중 에러 발생: " + e.getMessage());

        // 테이블 드랍
            stylistDao.dropTable("Category");
        }
        
        // 브랜드 테이블 적재
        try {
            for(int i = 0; i < prices.length; i++) {

                // 브랜드 명을 A~I 알파벳 순서대로 적재
                String name = String.valueOf((char) ('A' + i));
                List<BrandCategoryDto> categories = new ArrayList<>();

                // 카테고리별 금액을 순서대로 적재
                for (int j = 0; j < CATEGORIES.size(); j++) {
                    categories.add(new BrandCategoryDto((long)i+1, CATEGORIES.get(j), prices[i][j]));
                }

                BrandDto brandDto = new BrandDto();
                brandDto.setBrandId((long)i+1);
                brandDto.setName(name);
                brandDto.setCategories(categories);

                stylistDao.insertBrand(brandDto);
                stylistDao.insertBrandCategories(categories);
                // log.info(">>>>>>>>>>>"  i  "번째 적재 완료");
            }
        } catch (Exception e) {
            log.info("InitDataLoader 중 에러 발생: " + e.getMessage());

                // 테이블 드랍
                stylistDao.dropTable("Brand");
                stylistDao.dropTable("Brand_Category");
                stylistDao.dropTable("Category");
            }
        }

    // 종료 시 호출되는 메서드
    @PreDestroy
    public void cleanUp() {
        log.info("애플리케이션 종료 중, 데이터 삭제 시작...");
        stylistDao.dropTable("Brand");
        stylistDao.dropTable("Brand_Category");
        stylistDao.dropTable("Category");
    }
}