package musinsa.dao;

import lombok.RequiredArgsConstructor;
import musinsa.model.Brand;
import musinsa.model.Category;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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

        // todo 여기 구현하는 게 어려운듯
        for(String key : brand.getCategories().keySet()) {
            Map<String, Object> params = new HashMap<>();
            params.put("brandId", brand.getId());
            params.put("category", key);
            params.put("price", brand.getCategories().get(key));

            // todo 밑에 함수랑 중복되어 수정 필요
            sqlSession.insert("StylishDao.insertBrandCategory", params);
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

    public int findBrandByName(String name) {
        return sqlSession.selectOne("StylishDao.findBrandByName", name);
    }

    public List<Brand> getAllBrands() {
        return sqlSession.selectList("StylishDao.getAllBrands");
    }

    public void deleteBrand(Brand brand) {
        sqlSession.delete("StylishDao.deleteBrand");
        sqlSession.delete("StylishDao.deleteBrandCategory");
    }

//    public void deleteAll() {
//        sqlSession.delete("StylishDao.deleteAll");
//    }
}
