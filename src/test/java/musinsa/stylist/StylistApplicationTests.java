package musinsa.stylist;

import musinsa.dao.StylishDao;
import musinsa.model.Brand;
import musinsa.util.InitialData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest // 전체 컨텍스트 로딩
@Transactional // 각 테스트마다 롤백
class StylistApplicationTests {

	@Autowired
	private StylistService stylistService;

	static final List<String> CATEGORIES = Arrays.asList("상의", "아우터", "바지", "스니커즈", "가방", "모자", "양말", "액세서리");


}
