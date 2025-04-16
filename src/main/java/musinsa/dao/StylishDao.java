package musinsa.dao;

import lombok.RequiredArgsConstructor;
import musinsa.model.Brand;
import musinsa.model.Category;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StylishDao {
    // SqlSession을 사용하여 XML Mapper에 정의된 쿼리 호출
    private final SqlSession sqlSession;

    public void insertBrand(Brand brand) {
        sqlSession.insert("StylishDao.insertBrand", brand);

        for(Category category : brand.getCategories()) {
            Map<String, Object> params = new HashMap<>();
            params.put("brandId", brand.getId());
            params.put("category", category.getName());
            params.put("price", category.getPrice());

            sqlSession.insert("StylishDao.insertCategory", params);
        }
    }

    public void insertBrandCategory(Long brandId, String category, Integer price) {
        sqlSession.insert("StylishDao.insertBrandCategory",
                Map.of("brandId", brandId,
                        "category", category,
                        "price", price));
    }

    public List<Map<String, Object>> getResultByCategory(String category) {
        return sqlSession.selectList("StylishDao.getResultByCategory", category);
    }

    public List<Brand> getAllBrands() {
        return sqlSession.selectList("StylishDao.getAllBrands");
    }

    public void deleteAll() {
        sqlSession.delete("StylishDao.deleteAll");
    }
}
