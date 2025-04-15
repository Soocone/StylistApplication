package musinsa.util;

import musinsa.model.Brand;

import java.util.*;

/**
 * 초기 데이터 적재를 위한 class
 */
public class InitialData {

    // 카테고리명을 static 설정
    public static final List<String> categoryNames = Arrays.asList("상의", "아우터", "바지", "스니커즈", "가방", "모자", "양말", "액세서리");

    // 브랜드, 카테고리 별 금액 데이터 생성
    public static List<Brand> generate() {

        int[][] prices = {
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

        List<Brand> brands = new ArrayList<>();

        for(int i = 0; i < prices.length; i++) {
            // 브랜드 명을 A~I 알파벳 순서대로 적재
            String name = String.valueOf((char) ('A' + i));
            Map<String, Integer> categories = new LinkedHashMap<>();
            // 카테고리별 금액을 순서대로 적재
            for (int j = 0; j < categoryNames.size(); j++) {
                categories.put(categoryNames.get(j), prices[i][j]);
            }
            brands.add(new Brand(name, categories));
        }

        // 출력 확인
        for (Brand brand : brands) {
            System.out.println(brand);
        }

        return brands;
    }
}
