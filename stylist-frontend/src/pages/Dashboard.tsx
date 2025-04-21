import React, { useEffect, useState } from 'react';
import { fetchAllBrands } from '../api/stylistApi';
import BrandList from '../components/BrandList';
import BrandModal from '../components/BrandModal';
import SearchModal from '../components/SearchModal';
import LowestBrandModal from '../components/LowestBrandModal';
import LowestSumModal from '../components/LowestSumModal';
import TopCategoryModal from '../components/TopCategoryModal';
import SimulationModal from '../components/SimulationModal';
import { Brand } from '../types/models';
import { getbestBySingleBrand, getbestByCategory, gettopCategory, getrandomSimulation} from '../api/stylistApi';

const Dashboard = () => {
  const [brands, setBrands] = useState<Brand[]>([]);
  const [selectedBrand, setSelectedBrand] = useState<Brand | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isOpenSearchModal, setIsOpenSearchModal] = useState(false);
  const [isLowestBrandModalOpen, setIsLowestBrandModalOpen] = useState(false);
  const [isLowestSumModalOpen, setIsLowestSumModalOpen] = useState(false);
  const [isTopCategoryModalOpen, setIsTopCategoryModalOpen] = useState(false);
  const [isSimulationModalOpen, setIsSimulationModalOpen] = useState(false);
  const [selectedIds, setSelectedIds] = useState<number[]>([]);

  const [bestCategory, setBestCategory] = useState([]);
  const [bestBrand, setBestBrand] = useState({});
  const [topCategoryData, setTopCategoryData] = useState([]);
  const [simulationData, setSimulationData] = useState([]);

  useEffect(() => {
    // 전체 브랜드 목록 조회 API 호출
    fetchAllBrands().then(data => setBrands(data));
  }, []);

  // 브랜드 목록 더블클릭 이벤트
  const handleRowDoubleClick = (brand: Brand) => {
    setSelectedBrand(brand);
    setIsModalOpen(true);
  };

  // 카테고리별 최저가 브랜드 조회 버튼 클릭 이벤트
  const handleIsLowestBrandButtonClick = () => {
    fetchBestByCategory();
    setIsLowestBrandModalOpen(true);
  };

  // 전체 상품 구매 시 합계 최저가 브랜드 조회 버튼 클릭 이벤트
  const handleSingleBrandButtonClick = () => {
    fetchBestBySingleBrand();
    setIsLowestSumModalOpen(true);
  };

  // 브랜드 별 가장 비싸게 거래 중인 카테고리 버튼 클릭 이벤트
  const handleTopCategoryButtonClick = () => {
    fetchGettopCategory();
    setIsTopCategoryModalOpen(true);
  };

  // 돌려 돌려 랜덤 코디 버튼 클릭 이벤트
  const handleSimulationButtonClick = () => {
    fetchSimulation();
    setIsSimulationModalOpen(true);
  };

  // API 요청
  // 카테고리별로 최저 가격에 판매하는 브랜드를 조회하는 함수 호출
  const fetchBestByCategory = async () => {
    try {
      const response = await getbestByCategory();
      if (!response.success) {
        alert("조회에 실패했습니다.: " + response.message);
      } else {
        // console.log("데이터:", response.data);
        const best = response.data;
        setBestCategory(best);
      }
    } catch (err) {
      alert('조회 중 오류가 발생했습니다.');
      setBestCategory([]);
    }
  };

   // 단일 브랜드에서 전체 상품 구매 시 최저가에 제공하는 브랜드 조회 함수 호출
   const fetchBestBySingleBrand = async () => {
    try {
      const response = await getbestBySingleBrand();
      if (!response.success) {
        alert("조회에 실패했습니다.: " + response.message);
      } else {
        console.log("데이터:", response.data['최저가']);
        setBestBrand(response.data['최저가']);
      }
    } catch (err) {
      alert('요청 중 오류가 발생했습니다.');
      setBestBrand({});
    }
  };

  // 각 브랜드별로 평균 가격이 가장 높은 카테고리와 그 평균 가격 조회 함수 호출
  const fetchGettopCategory = async () => {
   try {
     const response = await gettopCategory();
     if (!response.success) {
       alert("조회에 실패했습니다.: " + response.message);
     } else {
       console.log("top 데이터:", response.data);
       setTopCategoryData(response.data);
     }
   } catch (err) {
     alert('요청 중 오류가 발생했습니다.');
     setTopCategoryData([]);
   }
 };

  // 랜덤 코디 시뮬레이션 함수 호출
  const fetchSimulation = async () => {
   try {
     const response = await getrandomSimulation();
     if (!response.success) {
       alert("조회에 실패했습니다.: " + response.message);
     } else {
       console.log("시뮬 데이터:", response.data);
       setSimulationData(response.data);
     }
   } catch (err) {
     alert('요청 중 오류가 발생했습니다.');
     setSimulationData([]);
   }
 };

  return (
  <div>
    {/* 브랜드 검색 영역 */}
    <div className="flex justify-end items-center">
      <div className="space-x-2">
        <button
          className="bg-blue-500 text-white px-4 py-2 rounded"
          onClick={() => setIsOpenSearchModal(true)}
        >
          🔍 최저/최고 가격 제공 브랜드 검색
        </button>
      </div>
    </div>
      {isOpenSearchModal && (
        <SearchModal
          onClose={() => {
            setIsOpenSearchModal(false);
          }} 
          />
      )}

      {/* 브랜드 목록 영역 */}
      <h1 className="text-2xl font-bold">📖 전체 브랜드 목록</h1>
      <BrandList 
        data={brands}
        onDoubleClick={handleRowDoubleClick}
        onSelectedChange={setSelectedIds}
        isAdmin={false}
      />
      {isModalOpen && (
        <BrandModal
          initialData={selectedBrand}
          onClose={() => {
            setIsModalOpen(false);
            fetchAllBrands().then(setBrands);
          }} 
          isOpen={isModalOpen}     
           />
      )}

      <h3 className="text-xl font-bold mb-1 mt-6 text-center">🔒 사용자가 궁금해 할만한 정보를 모아봤어요.</h3>

      {/* 버튼영역 */}
      <div className="flex justify-between mb-4">
        <div className="flex justify-center w-full space-x-2 mt-2">
          <button
            className="bg-gray-500 text-white px-4 py-2 rounded"
            onClick={()=>handleIsLowestBrandButtonClick()}
          >
            💥 카테고리별 최저가 브랜드 조회
          </button>
          <button
            className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-red-600"
            onClick={()=>handleSingleBrandButtonClick()}
          >
            💫 전체 상품 구매 시 합계 최저가 브랜드 조회
          </button>
          <button
            className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            onClick={()=>handleTopCategoryButtonClick()}
          >
            🔥 브랜드 별 가장 비싸게 거래 중인 카테고리
          </button>
          <button
            className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            onClick={()=>handleSimulationButtonClick()}
          >
            ❔ 돌려 돌려 랜덤 코디
          </button>
        </div>
      </div>

      {/* 모달영역 */}
      {isLowestBrandModalOpen && (
        <LowestBrandModal
          onClose={() => {
            setIsLowestBrandModalOpen(false);
          }} 
          data={bestCategory}
          />
      )}

      {isLowestSumModalOpen && (
        <LowestSumModal
          onClose={() => {
            setIsLowestSumModalOpen(false);
          }} 
          data={bestBrand}
          />
      )}

      {isTopCategoryModalOpen && (
        <TopCategoryModal
          onClose={() => {
            setIsTopCategoryModalOpen(false);
          }} 
          data={topCategoryData}
          />
      )}

      {isSimulationModalOpen && (
        <SimulationModal
          onClose={() => {
            setIsSimulationModalOpen(false);
          }} 
          data={simulationData}
          />
      )}
    </div>
  );
};

export default Dashboard;