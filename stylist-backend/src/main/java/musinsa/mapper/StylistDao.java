package musinsa.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;
import musinsa.dto.BrandCategoryDto;
import musinsa.dto.BrandDto;
import musinsa.dto.CategoryDto;

@Mapper
public interface StylistDao {

    void createBrandTable();

    void createCategoryTable();

    void createBrandCategoryTable();

    List<Map<String, Object>> getResultByCategory(String category);

    int findBrandByName(String name);

    List<Map<String, Object>> getDashboard();

    List<Map<String, Object>> getAllInfo();

    List<Map<String, Object>> getAllBrands();

    List<Map<String, Object>> getBrandCategoryPrices(Long brandId);

    List<Map<String, Object>> getAverageCategoryPrices();

    List<Map<String, Object>> getBrandsByCategory(String category);

    List<Map<String, Object>> getTopCategoryByBrand();

    String getBrandNameById(Long brandId);

    void insertBrand(BrandDto brandDto);

    void insertBrandCategories(List<BrandCategoryDto> categories);

    void insertCategories(CategoryDto categoryDto);

    void updateBrand(BrandDto brandDto);

    void updateBrandCategories(BrandCategoryDto categories);

    void deleteBrandCategories(List<Long> brandDtos);

    void deleteBrandById(List<Long> brandDtos);

    void deleteAll();

    void dropTable(String tableName);
}