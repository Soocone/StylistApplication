import React, { useEffect, useState } from 'react';
import AnalysisDetail from '../components/AnalysisDetail';
import { getbestByCategory, getbestBySingleBrand } from '../api/stylistApi';

const Analysis: React.FC = () => {
  const [bestCategory, setBestCategory] = useState([]);
  const [bestBrand, setBestBrand] = useState({});
  const [bestResultByCategory, setBestResultByCategory] = useState([]);

  useEffect(() => {
    // 카테고리별로 최저 가격에 판매하는 브랜드 조회 API 호출하여 props로 전달
    getbestByCategory().then(data => setBestCategory(data));

    // 단일 브랜드에서 전체 상품 구매 시 최저가에 제공하는 브랜드 조회 API 호출하여 props로 전달
    getbestBySingleBrand().then((data) => {
      const best = data['최저가'];
      setBestBrand(best);
    });
  }, []);

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-6">🔒 현재 당신이 궁금해 할 정보를 모아봤어요.</h1>
      <AnalysisDetail title="📌 카테고리별로 최저 가격에 판매하는 브랜드" data={bestCategory} />
      <AnalysisDetail title="📌 단일 브랜드에서 전체 상품 구매 시 최저가에 제공하는 브랜드" data={bestBrand} />
      <AnalysisDetail title="📌 검색을 통한 최저/최고 가격 제공 브랜드" data={bestResultByCategory} />
    </div>
  );
};

export default Analysis;