package musinsa.controller;

 import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import musinsa.dto.BrandDto;
import musinsa.dto.CategoryDto;
import musinsa.service.StylistService;
import musinsa.util.ApiResponse;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@MapperScan("musinsa.mapper")
@RequestMapping("/stylist")
public class StylistController {

    @Autowired
    private StylistService stylistService;

    // 전체 브랜드별 상품 조회
    @GetMapping("/allInfo")
    public List<Map<String, Object>> getAll() {
        return stylistService.getAll();
    }

    // 모든 브랜드 조회
    @GetMapping("/allBrands")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAllBrands(){
        List<Map<String, Object>> result = stylistService.getAllBrands();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
    @GetMapping("/analysis/bestByCategory")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBestPriceByCategory(){
        Map<String, Object> result = stylistService.getBestPriceByCategory();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회
    @GetMapping("/analysis/bestBySingleBrand")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBestPriceFromSingleBrand(){
        Map<String, Object> result = stylistService.getBestPriceFromSingleBrand();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
    @PostMapping("/analysis/bestResultBy")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getResultByCategory(@Valid @RequestBody CategoryDto categoryDto) {
        Map<String, Object> result = stylistService.getResultByCategory(categoryDto);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 코디 시뮬레이션
    @GetMapping("/analysis/simulation")
    public ResponseEntity<ApiResponse<Map<String, Object>>> simulation() {
        Map<String, Object> result = stylistService.simulation();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 각 브랜드별로 평균 가격이 가장 높은 카테고리와 그 평균 가격 조회
    @GetMapping("/analysis/topCategory")
    public ResponseEntity<?> getTopCategoryByBrand() {
        List<Map<String, Object>> result = stylistService.getTopCategoryByBrand();
        return ResponseEntity.ok().body(ApiResponse.success(result));
    }

    // 평균 대비 브랜드 가격 비교
    @PostMapping("/compare")
    public ResponseEntity<ApiResponse<Map<String, Object>>> compareBrandPrice(@Valid @RequestBody BrandDto brandDto) {
        Map<String, Object> result = stylistService.compareBrandPrice(brandDto);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 브랜드 및 상품을 등록
    @PostMapping("/insert")
    public ResponseEntity<ApiResponse<String>> insertBrand(@Valid @RequestBody BrandDto brandDto) {
        log.info(String.valueOf(brandDto));
        stylistService.insertBrand(brandDto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 브랜드 및 상품을 수정
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<String>> updateBrand(@Valid @RequestBody BrandDto brandDto) {
        stylistService.updateBrand(brandDto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 브랜드 및 상품 삭제
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteBrands(@Valid @RequestBody List<Long> brandIds) {
        stylistService.deleteBrand(brandIds);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping
    public void deleteAll() {
        stylistService.deleteAll();
    }
}