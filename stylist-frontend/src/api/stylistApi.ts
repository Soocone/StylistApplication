import axios from 'axios';

/**
 * API 목록
 */

// 전체 브랜드별 상품 조회
export const fetchAllBrands = async () => {
  const res = await axios.get('/stylist/allInfo');
  return res.data;
};

// 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
export const getbestByCategory = async () => {
  const res = await axios.get('/stylist/analysis/bestByCategory');
  return res.data;
};

// 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회
export const getbestBySingleBrand = async () => {
  const res = await axios.get('/stylist/analysis/bestBySingleBrand');
  return res.data;
};

// 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
export const getbestResultByCategoryName = async (categoryName: string) => {
  const res = await axios.post('/stylist/analysis/bestResultBy', {
    name: categoryName
  });
  return res.data;
};

// 브랜드별 카테고리 가격 비교 결과 조회
export const getComparison = async (brandId: string) => {
  const res = await axios.post('/stylist/compare', {
    brandId: brandId
  });
  return res.data;
};

// 각 브랜드별로 평균 가격이 가장 높은 카테고리와 그 평균 가격 조회
export const gettopCategory = async () => {
  const res = await axios.get('/stylist/analysis/topCategory');
  return res.data;
};

// 랜덤 코디 시뮬레이션 조회
export const getrandomSimulation = async () => {
  const res = await axios.get('/stylist/analysis/simulation');
  return res.data;
};

// 전체 브랜드 조회
export const getAllBrands = async () => {
  const res = await axios.get('/stylist/allBrands');
  return res.data;
};

// 브랜드 등록
export const insertBrand = (data: any) => axios.post('/stylist/insert', data);

// 브랜드 수정
export const updateBrand = (data: any) => axios.post('/stylist/update', data);

// 브랜드 삭제
export const deleteBrand = (data: any) => axios.post('/stylist/delete', data);