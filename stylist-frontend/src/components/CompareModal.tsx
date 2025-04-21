import React, { useEffect, useState } from "react";
import { getComparison, getAllBrands } from '../api/stylistApi';
import { Brand } from "../types/models";

interface CategoryComparison {
  brandPrice: number;
  averagePrice: number;
  diffPercent: string;
}

interface ComparisonResult {
  brandName: string;
  priceComparison: {
    [category: string]: CategoryComparison;
  };
}

interface CompareModalProps {
    onClose: () => void;
    data: Brand[]
}

const CompareModal: React.FC<CompareModalProps> = ({onClose, data}) => {
  const [selectedBrandId, setSelectedBrandId] = useState<string>('');
  const [comparison, setComparison] = useState<ComparisonResult | null>(null);
  const [error, setError] = useState<string>("");

  useEffect(() => {
  }, [data])

  useEffect(() => {
    // 비교 API 호출
    const fetchComparison = async () => {
      try {
        const res = await getComparison(selectedBrandId);
        if (res.success) {
          setComparison(res.data);
          setError("");
        } else {
          setComparison(null);
          setError(res.message || "데이터를 불러올 수 없습니다.");
        }
      } catch (err) {
        setComparison(null);
        setError("서버 오류가 발생했습니다.");
      }
    };
  
    if (selectedBrandId) {
      fetchComparison();
    } else {
      // 선택 초기화 시 이전 결과 제거
      setComparison(null);
    }
  }, [selectedBrandId]);


  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded shadow-lg w-[500px]">
        <div className="p-6">
          <h2 className="text-xl font-semibold mb-4">💸 평균 대비 가격 비교 해드려요</h2>
          {/* 에러 or 로딩 메시지 */}
          {error ? (
            <div className="text-red-500 p-4">{error}</div>
          ) : !comparison && selectedBrandId ? (
            <div className="p-4 text-gray-500">로딩 중...</div>
          ) : null}
          {/* 브랜드 선택 */}
          <div className="mb-6">
            <label className="block mb-2 text-sm font-medium">브랜드 선택</label>
            <select
              className="border rounded p-2 w-full"
              onChange={(e) => setSelectedBrandId(e.target.value)}
              defaultValue=""
            >
              <option value="" disabled>
                브랜드를 선택하세요
              </option>
              {data && data.length > 0 ? (
                data.map((brand) => (
                  <option key={brand.brandId} value={brand.brandId}>
                    {brand.brandName}
                  </option>
                ))
              ) : (
                <option disabled>브랜드 데이터가 없습니다</option>
              )}
            </select>

            {/* 에러 메시지 */}
            {error && <div className="text-red-500 mb-4">{error}</div>}
          </div>

          {/* 비교 결과 테이블 */}
          {comparison && (
            <div>
              <h3 className="text-lg font-semibold mb-2">
                평균 대비 {comparison.brandName} 브랜드 가격 비교 결과
              </h3>
              <table className="w-full table-auto border border-gray-300">
                <thead className="bg-gray-100">
                  <tr>
                    <th className="p-2 border text-center">카테고리</th>
                    <th className="p-2 text-center border">브랜드 가격</th>
                    <th className="p-2 text-center border">평균 가격</th>
                    <th className="p-2 border text-center">차이(%)</th>
                  </tr>
                </thead>
                <tbody>
                  {Object.entries(comparison.priceComparison).map(([category, data]) => (
                    <tr key={category}>
                      <td className="p-2 border text-center">{category}</td>
                      <td className="p-2 text-right border">{typeof data.brandPrice === 'number' ? data.brandPrice.toLocaleString() + '원' : '-'}</td>
                      <td className="p-2 text-right border">{typeof data.averagePrice === 'number' ? data.averagePrice.toLocaleString() + '원' : '-'}</td>
                      <td
                        className={`p-2 text-right border ${
                          typeof data.diffPercent === "string" && data.diffPercent.startsWith("+")
                            ? "text-green-600"
                            : "text-red-600"
                        }`}
                      >
                        {data.diffPercent}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
          <div className="mt-4 flex justify-end gap-2">
            <button className="px-4 py-2 bg-gray-300 rounded" onClick={onClose}>닫기</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CompareModal;