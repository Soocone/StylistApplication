import React  from 'react';

interface TopCategoryModalProps {
  onClose: () => void;
  data: any
}

const TopCategoryModal: React.FC<TopCategoryModalProps> = ({ onClose, data }) => {

    return (
      <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
      <div className="bg-white p-4 rounded shadow-md mb-6">
      <h2 className="text-xl font-bold mb-4">🔥 브랜드별 평균 가격이 가장 높은 카테고리를 보여드릴게요.</h2>

      <table className="w-full table-auto border-collapse">
        <thead>
          <tr className="bg-gray-100">
            <th className="border px-4 py-2">브랜드</th>
            <th className="border px-4 py-2">카테고리</th>
            <th className="border px-4 py-2">평균 가격</th>
          </tr>
        </thead>
        <tbody>
          {data.map((item: any, idx: number) => (
            <tr key={idx} className="hover:bg-gray-50">
              <td className="border px-4 py-2 text-left">{item.BRAND_NAME}</td>
              <td className="border px-4 py-2 text-center">{item.CATEGORY_NAME}</td>
              <td className="border px-4 py-2 text-right">{item.AVG_PRICE.toLocaleString()}원</td>
            </tr>
          ))}
        </tbody>
      </table>
          <div className="mt-4 flex justify-end gap-2">
            <button className="px-4 py-2 bg-gray-300 rounded" onClick={onClose}>닫기</button>
          </div>
      </div>
      </div>
    );
};

export default TopCategoryModal;