import React, { useState, useEffect } from 'react';
import { getbestResultByCategoryName} from '../api/stylistApi';

interface SearchModalProps {
    onClose: () => void;
}

const SearchModal: React.FC<SearchModalProps> = ({ onClose }) => {

  const [categoryName, setCategoryName] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [data, setData] = useState({} as any);

  useEffect(() => {
    fetchBestByCategoryName();
    }, []);
  
  // 유효성 체크
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
  
      // 유효하면 조회 함수 실행
      fetchBestByCategoryName();
    };
  };
    
  // 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 함수 호출
  const fetchBestByCategoryName = async () => {
    if (!categoryName) return;

    setLoading(true);
    setError('');

    // API 요청
    try {
      const response = await getbestResultByCategoryName(categoryName);
      if (!response.success) {
        setLoading(false);
        setError(response.message);
      } else {
        console.log("데이터:", response.data);
        setLoading(false);
        setData(response.data);
      }
    } catch (err) {
      alert('요청 중 오류가 발생했습니다.');
      setLoading(false);
      setError('조회에 실패했습니다.');
      setData({});
    }
  };


    return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
    <div className="bg-white p-4 rounded shadow-md mb-6">
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
          <button className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600" onClick={validChk(categoryName)}>조회</button>
          <button className="px-4 py-2 bg-gray-300 rounded" onClick={onClose}>닫기</button>
        </div>
        <div className="mt-4 flex justify-center gap-2">
            {loading && <span className="ml-2 text-gray-500">조회 중...</span>}
            <span className="ml-2 text-red-500">{error}</span>
          </div>

        {/* 조회 결과 영역 */}
        {Array.isArray(data.최저가) && Array.isArray(data.최고가) && (
          <div className="bg-white p-4 rounded shadow-md">
            <h3 className="text-lg font-semibold mb-2">카테고리: {data.카테고리}</h3>

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
                  {data.최저가.map((item: any, idx: number) => (
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
                  {data.최고가.map((item: any, idx: number) => (
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
      </div>
      </div>
    );
};

export default SearchModal;