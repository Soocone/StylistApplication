package musinsa.stylist;

import musinsa.dto.BrandCategoryDto;
import musinsa.dto.BrandDto;
import musinsa.dto.CategoryDto;
import musinsa.mapper.StylistDao;
import musinsa.service.StylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

@ActiveProfiles("test")
@SpringBootTest // 전체 컨텍스트 로딩
@Transactional(rollbackFor = Exception.class)
public class StylistApplicationTests {

	@Autowired
	private StylistService stylistService;
 
	@Autowired
	private StylistDao stylistDao;

	static final List<String> CATEGORIES = Arrays.asList("상의", "아우터", "바지", "스니커즈", "가방", "모자", "양말", "액세서리");
 
	@BeforeEach
	void setup() {
	}

	@Test
	@DisplayName("1. 브랜드 전체 조회 성공")
	void testGetAllBrands() {
		// when
		List<Map<String, Object>> brands = stylistService.getAllBrands();
		
		// then
		assertThat(brands).isNotEmpty();
		assertThat(brands.size()).isEqualTo(9); // A~I까지 9개
	}

	@Test
	@DisplayName("2. 카테고리별 최저가 브랜드 리스트 조회 성공")
	void testGetBestPriceByCategory() {
		// when
		var result = stylistService.getBestPriceByCategory();

		// then
		assertThat(result).isNotEmpty();
		assertThat(result.size()).isEqualTo(9);
	}

	@Test
	@DisplayName("3. 카테고리별 최저가 조회 성공")
	public void testGetBestPriceByPart() {
		// given
		CategoryDto categoryDto = new CategoryDto(1L, "상의");

		// when
		Map<String, Object> result = stylistService.getResultByCategory(categoryDto);

		// then
		assertThat(result).containsKeys("카테고리", "최저가", "최고가");

		// 최저가 리스트에서 첫 번째 항목 꺼내기
		List<Map<String, Object>> lowList = (List<Map<String, Object>>) result.get("최저가");
		Map<String, Object> lowInfo = lowList.get(0);

		String brand = (String) lowInfo.get("BRAND");
		Integer price = ((Number) lowInfo.get("PRICE")).intValue();

		assertThat(brand).isEqualTo("C");
		assertThat(price).isEqualTo(10000);
	}

	@Test
	@DisplayName("4. 단일 브랜드 기준 최저금액, 총액 조회 성공")
	public void testGetBestSingleCompanyPrice() {
		// when
		Map<String, Object> result = stylistService.getBestPriceFromSingleBrand();
		
		// then
		assertThat(result).containsKeys("최저가");
		Map<String, Object> lowestPrice = (Map<String, Object>) result.get("최저가");

		String brand = (String) lowestPrice.get("브랜드");
		List<Map<String, String>> categories = (List<Map<String, String>>) lowestPrice.get("카테고리");
		String totalStr = lowestPrice.get("총액").toString();

		assertThat(brand).isEqualTo("D");
		assertThat(categories).hasSize(8);
		assertThat(totalStr).isEqualTo("36100");
	}

	@Test
	@DisplayName("5. 카테고리별 최저/최고가 조회 성공")
	public void testGetPriceRange() {
		// given
		CategoryDto categoryDto = new CategoryDto(1L, "상의");

		//when
				Map<String, Object> result = stylistService.getResultByCategory(categoryDto);

		// then
		// 필수 key 값 체크
		assertThat(result).containsKeys("카테고리", "최저가", "최고가");

		// 카테고리명이 맞는지
		assertThat(result.get("카테고리")).isEqualTo("상의");

		// 최저가/최고가 리스트가 비어 있지 않아야 함
		List<Map<String, Object>> lowest = (List<Map<String, Object>>) result.get("최저가");
		List<Map<String, Object>> highest = (List<Map<String, Object>>) result.get("최고가");

		assertThat(lowest).isNotEmpty();
		assertThat(highest).isNotEmpty();

		// 내부 값 타입 확인
		assertThat(lowest.get(0)).containsKeys("BRAND", "PRICE");
		assertThat(highest.get(0)).containsKeys("BRAND", "PRICE");
	}

	@Test
	@DisplayName("6. 브랜드 수정 테스트 성공")
	public void testModifyBrand() {
		// given
		BrandDto brandDto = new BrandDto();
		brandDto.setBrandId(1L);
		brandDto.setName("A");
		
		List<BrandCategoryDto> categories = new ArrayList<>();

		Random ranNum = new Random();

		for (String category : CATEGORIES) {
			categories.add(new BrandCategoryDto(1L, category, ranNum.nextLong(10000, 50000)));
		}
		brandDto.setCategories(categories);
		
		// when
		stylistService.updateBrand(brandDto);
		Map<String, Object> bestBrand = stylistService.getBestPriceFromSingleBrand();
		
		Map<String, Object> bestBrandInner = (Map<String, Object>) bestBrand.get("최저가");
		String brandName = (String) bestBrandInner.get("브랜드");

		// then
		assertThat(brandName).isEqualTo("D");

		List<Long> param = new ArrayList<>();
		param.add(1L);
		stylistService.deleteBrand(param);
		
		Map<String, Object> afterDelete = stylistService.getBestPriceFromSingleBrand();
		Map<String, Object> afterDeleteInner = (Map<String, Object>) afterDelete.get("최저가");
		String afterBrandName = (String) afterDeleteInner.get("브랜드");

		assertThat(afterBrandName).isNotEqualTo("A");
	}

		
	@Test
	@DisplayName("7. 브랜드 가격 전체 평균과 비교 성공")
	void testCompareBrandPrice() {
		// given
		BrandDto dto = new BrandDto();
		dto.setBrandId(1L); // A 브랜드
		
		// when
		Map<String, Object> result = stylistService.compareBrandPrice(dto);

		// then
		assertThat(result).isNotNull();
		assertThat(result).containsKeys("brandName", "priceComparison");

		// brandName 검증
		String brandName = (String) result.get("brandName");
		assertThat(brandName).isEqualTo("A");
		
		// priceComparison 검증
		@SuppressWarnings("unchecked")
		Map<String, Map<String, Object>> comparison = (Map<String, Map<String, Object>>) result.get("priceComparison");

		assertThat(comparison).hasSize(8); // 8개의 카테고리

		for (String category : CATEGORIES) {
			assertThat(comparison).containsKey(category);
			Map<String, Object> data = comparison.get(category);
			assertThat(data).containsKeys("brandPrice", "averagePrice", "diffPercent");

			Object brandPrice = data.get("brandPrice");
			Object avgPrice = data.get("averagePrice");
			Object diffPercent = data.get("diffPercent");

			// 가격 정보가 없는 경우는 "N/A"로 세팅되어 있음
			assertThat(brandPrice).isInstanceOfAny(Integer.class, String.class);
			assertThat(avgPrice).isInstanceOfAny(Integer.class, String.class);
			assertThat(diffPercent).isInstanceOfAny(String.class);
		}
	}

	@Test
	@DisplayName("8. 카테고리별 랜덤 코디 시뮬레이션 테스트")
	public void simulation() {
		// when
		Map<String, Object> result = stylistService.simulation();

		// then
		assertThat(result).isNotNull();
		assertThat(result).isInstanceOf(Map.class);
		
		// 총액 확인
		assertThat(result).containsKey("total");
		assertThat(result.get("total")).isInstanceOf(Integer.class);
		assertThat((Integer) result.get("total")).isGreaterThanOrEqualTo(0);
		
		// 카테고리별 결과 확인
		for (String category : CATEGORIES) {
			assertThat(result).containsKey(category);
			Object value = result.get(category);

			if (value instanceof Map) {
				Map<String, Object> brandInfo = (Map<String, Object>) value;
				assertThat(brandInfo).containsKeys("brand", "price");
				assertThat(brandInfo.get("brand")).isInstanceOf(String.class);
				assertThat(brandInfo.get("price")).isInstanceOf(Integer.class);
			}
		}
	}

		
	@Test
	@DisplayName("9. 각 브랜드별로 평균 가격이 가장 높은 카테고리와 그 평균 가격 조회")
	public void testGetTopCategoryByBrand() {
		
		List<Map<String, Object>> result = stylistService.getTopCategoryByBrand();

		assertNotNull(result);
		assertThat(result.isEmpty());

		for (Map<String, Object> item : result) {
			assertThat(item.containsKey("brand_name"));
			assertThat(item.containsKey("category_name"));
			assertThat(item.containsKey("avg_price"));
		}
	}

		
	@Test
	@DisplayName("10. 브랜드 삭제 기능")
	void testDeleteBrands() {
		// given
		List<Long> ids = List.of(7L); // I 브랜드

		// when
				stylistService.deleteBrand(ids);

		var deleted = stylistDao.getBrandNameById(7L);

		// then
		assertThat(deleted).isNull();
	}
}
