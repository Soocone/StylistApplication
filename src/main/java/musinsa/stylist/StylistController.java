package musinsa.stylist;

import musinsa.model.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class StylistController {

    @Autowired
    private StylistService stylistService;

    // 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
    @GetMapping("/bestByCategory")
    public Map<String, Object> getBestPriceByCategory(){
        return stylistService.getBestPriceByCategory();
    }
}
