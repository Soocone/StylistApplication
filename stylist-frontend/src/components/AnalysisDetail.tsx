import React, { useState } from 'react';
import { getbestResultByCategoryName } from '../api/stylistApi';

interface AnalysisDetailProps {
  title: string;
  data: any;
}

const AnalysisDetail: React.FC<AnalysisDetailProps> = ({ title, data }) => {
  const [categoryName, setCategoryName] = useState('');
  const [dynamicData, setDynamicData] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const validChk = (value: string) => {
    return () => {
      const trimmed = value.trim();
      const specialCharRegex = /[^가-힣a-zA-Z0-9\s]/g; // 한글, 영문, 숫자, 공백 외
  
      if (!trimmed) {
        alert('카테고리명을 입력해주세요.');
        return;
      }
      if (specialCharRegex.test(trimmed)) {
        alert('특수문자는 입력할 수 없습니다.');
        setCategoryName(''); // 검색어 초기화
        return;
      }
  
      fetchBestByCategory(); // 유효하면 조회 함수 실행
    };
  };

  // 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 함수 호출
  const fetchBestByCategory = async () => {
    if (!categoryName) return;

    setLoading(true);
    setError('');

    // API요청
    try {
      const response = await getbestResultByCategoryName(categoryName);
      if (!response.success) {
        alert("조회에 실패했습니다.: " + response.message);
      } else {
        console.log("데이터:", response.data);
        setLoading(false);
        setDynamicData(response.data);
      }
    } catch (err) {
      alert('요청 중 오류가 발생했습니다.');
      setLoading(false);
      setError('조회에 실패했습니다.');
      setDynamicData({});
    }
  };

  // 실제 보여줄 데이터: 동적 데이터가 있으면 보여주고 아니면 기본 props.data
  const displayData = title === '📌 검색을 통한 최저/최고 가격 제공 브랜드' && dynamicData ? dynamicData : data;


  return (
    <div className="bg-white p-4 rounded shadow-md mb-6">
      <h3 className="text-lg font-semibold mb-3">{title}</h3>

      {title === '📌 카테고리별로 최저 가격에 판매하는 브랜드' && data.data && (
        <div className="bg-white p-4 rounded shadow-md w-full max-w-2xl mx-auto">
          <table className="w-full table-auto border-collapse">
            <thead>
              <tr className="bg-gray-100">
                <th className="border px-4 py-2 text-center">카테고리</th>
                <th className="border px-4 py-2 text-center">브랜드</th>
                <th className="border px-4 py-2 text-center">가격</th>
              </tr>
            </thead>
            <tbody>
              {Object.entries(data.data)
                .filter(([key]) => key !== 'total') // total 제외
                .map(([category, detail]: any, idx) => (
                  <tr key={idx} className="hover:bg-gray-50">
                    <td className="border px-4 py-2 text-center">{category}</td>
                    <td className="border px-4 py-2">{detail.BRAND}</td>
                    <td className="border px-4 py-2 text-right">
                      {typeof detail.PRICE === 'number' ? detail.PRICE.toLocaleString() + '원' : '-'}
                    </td>
                  </tr>
                ))}
              <tr className="font-semibold bg-blue-50">
                <td className="border px-4 py-2 text-center" colSpan={2}>
                  총액
                </td>
                <td className="border px-4 py-2 text-right">
                  {typeof data.data.total === 'number' ? data.data.total.toLocaleString() + '원' : '-'}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      )}



    {title === '📌 단일 브랜드에서 전체 상품 구매 시 최저가에 제공하는 브랜드' && data && (
        <div className="bg-white p-4 rounded shadow-md w-full max-w-2xl mx-auto">
      <>
        <h2>브랜드명: {data.브랜드}</h2>
        <h3>총액: {data.총액 ? Number(data.총액).toLocaleString() + '원' : '-'}</h3>
        {Array.isArray(data.카테고리) && (
          <table className="w-full table-auto border-collapse mt-4">
            <thead>
              <tr className="bg-gray-100">
                <th className="border px-4 py-2 text-center">카테고리</th>
                <th className="border px-4 py-2 text-center">가격</th>
              </tr>
            </thead>
            <tbody>
              {data.카테고리.map((item: any, idx: number) => (
                <tr key={idx}>
                  <td className="border px-4 py-2 text-center">{item.카테고리}</td>
                  <td className="border px-4 py-2 text-right">{item.가격 ? Number(item.가격).toLocaleString() + '원' : '-'}</td>
                  
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </>
      </div>
    )}


      {title === '📌 검색을 통한 최저/최고 가격 제공 브랜드' && displayData && (
      <div className="mb-4">
        {/* 검색 영역 */}
        <div className="flex gap-2 mb-4">
          <input
            type="text"
            value={categoryName}
            onChange={(e) => setCategoryName(e.target.value)}
            placeholder="카테고리명을 입력하세요 (예: 상의)"
            className="border px-3 py-2 rounded w-64"
          />
          <button
            onClick={validChk(categoryName)}
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            조회
          </button>
          {loading && <span className="ml-2 text-gray-500">조회 중...</span>}
          {error && <span className="ml-2 text-red-500">{error}</span>}
        </div>

        {/* 조회 결과 영역 */}
        {Array.isArray(displayData.최저가) && Array.isArray(displayData.최고가) && (
          <div className="bg-white p-4 rounded shadow-md">
            <h3 className="text-lg font-semibold mb-2">카테고리: {displayData.카테고리}</h3>

            <div className="mb-4">
              <h4 className="font-semibold text-blue-600 mb-1">최저가 브랜드</h4>
              <table className="w-full table-auto border-collapse mb-2">
                <thead>
                  <tr className="bg-gray-100">
                    <th className="border px-4 py-2">브랜드</th>
                    <th className="border px-4 py-2">가격</th>
                  </tr>
                </thead>
                <tbody>
                  {displayData.최저가.map((item: any, idx: number) => (
                    <tr key={idx} className="hover:bg-gray-50">
                      <td className="border px-4 py-2">{item.BRAND}</td>
                      <td className="border px-4 py-2">{item.PRICE.toLocaleString()}원</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <div>
              <h4 className="font-semibold text-red-600 mb-1">최고가 브랜드</h4>
              <table className="w-full table-auto border-collapse">
                <thead>
                  <tr className="bg-gray-100">
                    <th className="border px-4 py-2">브랜드</th>
                    <th className="border px-4 py-2">가격</th>
                  </tr>
                </thead>
                <tbody>
                  {displayData.최고가.map((item: any, idx: number) => (
                    <tr key={idx} className="hover:bg-gray-50">
                      <td className="border px-4 py-2">{item.BRAND}</td>
                      <td className="border px-4 py-2">{item.PRICE.toLocaleString()}원</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </div>
      )}
    </div>
  );
};

export default AnalysisDetail;