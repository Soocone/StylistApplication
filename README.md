
# 무신사의 차기작! 코디 완성 서비스 🕶️ Stylist Application

> 고객과 관리자 모두가 원하는 **데이터 제공 및 관리** 기능을 제공하는 코디 완성 서비스입니다.

---

## ✅ 구현 범위

### 🔧 백엔드 (Spring Boot + MyBatis)

**고객을 위한 기능**
- 브랜드별 전체 가격 분석
- 총합 최저가 브랜드 도출
- 카테고리별 최저가 / 최고가 브랜드 도출
- 서비스의 목적인 랜덤 코디 제공(추가 개발)
- 브랜드 별 가장 고가에 거래 중인 카테고리 도출 (추가 개발)

**관리자를 위한 기능**
- 브랜드 및 카테고리별 가격 등록 / 수정 / 삭제
- 특정 브랜드의 카테고리별 가격을 평균 가격과 비교 (추가 개발)

**그 외 기능**
- 애플리케이션 실행 시 초기 데이터 자동 적재, 오류/종료 시 자동 삭제
- API 실패 시 `ResponseEntity`를 통해 실패 사유 응답
- `JUnit 5` 기반 단위 테스트 및 통합 테스트 구현

---

### 💻 프론트엔드 (React + TypeScript + TailwindCSS)

- 브랜드 목록 조회/등록/수정/삭제 (체크박스 다중 선택)
- 분석 결과(최저가 브랜드 및 카테고리별 분석 등) 모달 형태 조회
- Post 요청 시 입력 값 유효성 검사
- 무신사 스타일의 심플한 UI  
  > *백엔드 중심 과제이므로 프론트 디자인이 비교적 아름답지 못한 점 양해 부탁드립니다.... 🙇‍♀️*

---

## 📁 ERD 구성


**(1) 브랜드 (BRAND)**
- 브랜드를 관리하는 독립 테이블
- 향후 담당자, 국가 등 상세정보 확장 가능성 고려

**(2) 카테고리 (CATEGORY)**
- "상의", "바지", "신발" 등 공통 카테고리 관리
- 중복 방지 및 유지보수 효율성 확보

**(3) 브랜드-카테고리 중간 테이블 (BRAND_CATEGORY)**
- 다대다 관계 해소 및 `price` 관리
- 향후 할인율, 재고 등의 속성 추가 가능
- **가격 분석 및 비교의 핵심 테이블**

---

## ⚙️ 개발 환경

### 백엔드
- Java 17
- Spring Boot 3.4.4
- MyBatis 3.5.13
- H2 Database

### 프론트엔드
- React 18
- TypeScript
- TailwindCSS

---

## 📂 프로젝트 구조

\`\`\`
StylistApplication
├── backend
│   ├── controller
│   ├── service
│   ├── mapper
│   └── dto
├── frontend
│   ├── components
│   ├── pages
│   ├── api
│   └── types
\`\`\`

---

## 🚀 빌드 및 실행 방법

### 1. 프로젝트 클론

\`\`\`bash
git clone https://github.com/Soocone/StylistApplication.git
cd StylistApplication
\`\`\`

### 2. 백엔드 실행

\`\`\`bash
cd backend
./gradlew build
./gradlew bootRun
\`\`\`

- 백엔드 기본 포트: `http://localhost:8080`

**H2 콘솔 접속**
- URL: `http://localhost:8080/h2-console`
- JDBC: `jdbc:h2:tcp://localhost/~/stylist`
- driver-class-name: `org.h2.Driver`
- User: `sa`
- Password: _(비워둠)_

### 3. 프론트엔드 실행

\`\`\`bash
cd frontend
npm install
npm start
\`\`\`

- 프론트 기본 포트: `http://localhost:3000`
- API 요청은 proxy 설정을 통해 `localhost:8080`으로 전달됨

### 4. 테스트 실행

\`\`\`bash
cd backend
./gradlew test
\`\`\`

---

## 🧩 개발 중 어려웠던 부분

### 선택한 브랜드의 카테고리 가격을 전체 평균과 비교

**어려운 이유**
- 선택 브랜드의 카테고리 가격, 전체 평균 가격을 조회 후
- 서비스 로직을 통해 가격 비교 정보를 return 해줘야 했기 때문에
- 로직을 깔끔하게 짜려면 Java 컬렉션 처리 및 도메인 이해 필요
- `int` vs `double` 혼용 시 소수점 처리나 캐스팅 문제/ 정확도 이슈 존재(아래 코드 참고)

**개선 방안**
- 비즈니스 로직은 서비스 계층에서 처리하고, DB는 단순 조회에 집중하게 설계
- List<Map<String, Object>> 같은 자료구조를 활용
- `BigDecimal`을 사용해 정확성 보장

(예시 코드)
\`\`\`java
// 평균과의 가격 비교 차이 담기 - ((브랜드가격 - 평균가격) / 평균가격) * 100
Map<String, Object> comparisonInfo = new LinkedHashMap<>();

// (개선 전)int / double 혼용
double diffPercent = ((brandPrice - avgPrice) / avgPrice) * 100;
String formatted = String.format("%.1f%%", diffPercent);
comparisonInfo.put("diffPercent", formatted);


// (개선 후)BigDecimal 사용
BigDecimal diff = brandPrice.subtract(avgPrice)
                .divide(avgPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

String formatted = diff.setScale(1, RoundingMode.HALF_UP).toString() + "%";
comparisonInfo.put("diffPercent", formatted);
\`\`\`

---

## ⭐ 핵심 기능: 고가 카테고리 분석 및 평균 가격 도출

- 가능한 조인을 모두 사용하고 상대적으로 복잡한 쿼리 작성을 통해 브랜드별 고가 카테고리 분석
- `PARTITION BY` + `RANK()`를 활용한 랭킹 기반 도출

\`\`\`sql
SELECT t.brand_id, t.brand_name, t.category_name, t.avg_price
FROM (
  SELECT bc.brand_id,
         b.name AS brand_name,
         c.category_id,
         c.name AS category_name,
         AVG(bc.price) AS avg_price,
         RANK() OVER (PARTITION BY bc.brand_id ORDER BY AVG(bc.price) DESC) AS rk
    FROM brand_category bc
    JOIN brand b ON bc.brand_id = b.brand_id
    JOIN category c ON bc.category = c.name
   GROUP BY bc.brand_id, bc.category
) t
WHERE t.rk = 1;
\`\`\`

> ⚠️ 초기 데이터로만 보면 무조건 "상의" 카테고리가 가장 비싸지만, 브랜드 가격 조정(수정 기능) 후 유의미한 결과 확인 가능

---

## ➕ 추가로 구현하고자 했던 로직들...

- 가격 변동 이력 테이블 추가하여 이력 적재 및 관리: 변화 추이, 분석 데이터 제공 가능
- 브랜드 별 가장 고가에 거래 중인 카테고리 확인(추가 개발 완료) 후 카테고리별 금액 일괄 조정 기능
