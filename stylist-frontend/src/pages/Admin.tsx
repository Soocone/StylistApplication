import React, { useEffect, useState } from 'react';
import { fetchAllBrands } from '../api/stylistApi';
import BrandList from '../components/BrandList';
import CompareModal from '../components/CompareModal';
import BrandModal from '../components/BrandModal';
import { Brand } from '../types/models';
import { deleteBrand, getAllBrands } from '../api/stylistApi';

const Admin = () => {
  const [brands, setBrands] = useState<Brand[]>([]);
  const [selectedBrand, setSelectedBrand] = useState<Brand | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedIds, setSelectedIds] = useState<number[]>([]);
  const [isCompareModal, setIsCompareModal] = useState(false);
  const [compareBrands, setCompareBrands] = useState<Brand[]>([]);
  const [error, setError] = useState<string>("");

  // 전체 브랜드 리스트 조회
  useEffect(() => {
    fetchAllBrands().then(data => setBrands(data));
  }, []);

  // 브랜드 리스트 로우 더블클릭 이벤트
  const handleRowDoubleClick = (brand: Brand) => {
    setSelectedBrand(brand);
    setIsModalOpen(true);
  };

  // 등록 버튼 클릭 이벤트
  const handleCreateClick = () => {
    setSelectedBrand(null);
    setIsModalOpen(true);
  };

  // 비교 버튼 클릭 이벤트
  const handleCompareBtnClick = () => {
    fetchCompareBrands();
    setIsCompareModal(true)
  };
  
  // 비교 모달용 전체 브랜드 목록 조회 API 호출
  const fetchCompareBrands = async () => {
    try {
      const res = await getAllBrands();
      if (res.success) {
        const normalizedBrands = res.data.map((b: any) => ({
          brandId: b.BRAND_ID,
          brandName: b.NAME
        }));
        setCompareBrands(normalizedBrands);
      } else {
        setError(res.message || "브랜드 목록을 불러올 수 없습니다.");
      }
    } catch (err) {
      setError("브랜드 목록 조회 중 오류가 발생했습니다.");
    }
  };

  // 브랜드 삭제 API 호출
  const handleDelete = async () => {
    if (selectedIds.length === 0) {
      alert('삭제할 브랜드를 선택하세요.');
      return;
    }

    try {
      await deleteBrand(selectedIds);
      alert('삭제되었습니다.');

      // 삭제된 브랜드 제외한 목록 갱신
      setBrands(prev => prev.filter(b => !selectedIds.includes(b.brandId!)));
      setSelectedIds([]);

    } catch (error) {
      alert('삭제 중 오류가 발생했습니다.');
    }
  };

  return (
  <div>
    {/* 브랜드 목록 영역 */}
      <div>
        <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">📖 전체 브랜드 목록</h1>
        <div className="flex space-x-2">
          <button
            className="bg-blue-500 text-white px-4 py-2 rounded"
            onClick={handleCreateClick}
          >
            신규 등록
          </button>
          <button
            className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
            onClick={handleDelete}
          >
            선택 삭제
          </button>
        </div>
      </div>
    </div>
      <BrandList 
        data={brands}
        onDoubleClick={handleRowDoubleClick}
        onSelectedChange={setSelectedIds}
        isAdmin={true}
      />
      {isModalOpen && (
        <BrandModal
          initialData={selectedBrand}
          onClose={() => {
            setIsModalOpen(false);
            fetchAllBrands().then(setBrands); // 상태 갱신
          } } 
          isOpen={isModalOpen}  
           />
      )}

      {/* 가격 비교 영역 */}
      <div className="flex justify-end mt-4">
        <div className="space-x-2 mb-2">
            <button
              className="bg-gray-500 text-white px-4 py-2 rounded"
              onClick={()=>handleCompareBtnClick()}
            >
              🔍 브랜드별 가격 비교하기
            </button>
        </div>
      </div>
        {isCompareModal && (
          <CompareModal
            onClose={() => {
              setIsCompareModal(false);
            }}
            data={compareBrands}
            />
        )}
    </div>
    
  );
};

export default Admin;