package musinsa.stylist;

import lombok.RequiredArgsConstructor;
import musinsa.model.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class StylistController {

    @Autowired
    private StylistService stylistService;

    // 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
    @GetMapping("/bestByCategory")
    public Map<String, Object> getBestPriceByCategory(){
        return stylistService.getResultByCategory();
    }

    // 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회
    @GetMapping("/bestBySingleBrand")
    public Map<String, Object> getBestPriceFromSingleBrand(){
        return stylistService.getBestPriceFromSingleBrand();
    }

    // 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
    @GetMapping("/bestResultBy/{categoryName}")
    public Map<String, Object> getResultByCategory(@RequestParam String categoryName) {
        return stylistService.getResultByCategory(categoryName);
    }

    // 브랜드 및 상품을 추가 / 업데이트
    @PostMapping
    public ResponseEntity<String> modifyBrands(@RequestBody List<Brand> brands) {
        stylistService.modifyBrands("수정", brands);
        return ResponseEntity.ok("브랜드 및 카테고리 정보가 업데이트되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteBrands(@RequestBody List<Brand> brands) {
        stylistService.modifyBrands("삭제", brands);
        return ResponseEntity.ok("브랜드 및 카테고리 정보가 삭제되었습니다.");
    }

//    @DeleteMapping
//    public void deleteAll() {
//        stylistService.clearAll();
//    }
}
