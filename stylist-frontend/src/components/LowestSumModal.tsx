import React from 'react';

interface LowestSumModalProps {
  onClose: () => void;
  data: any
}

const LowestSumModal: React.FC<LowestSumModalProps> = ({ onClose, data }) => {
  
    return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
    <div className="bg-white p-4 rounded shadow-md mb-6">
      <div className="bg-white p-4 rounded shadow-md w-full max-w-2xl mx-auto">
      <>
      <h2 className="text-xl font-semibold mb-3">💫 모든 카테고리 구매 시 총합 최저가인 브랜드예요.</h2>
        <h2 className="text-lg text-red-800 text-center">브랜드명: {data.브랜드}</h2>
        <h3 className="text-lg text-blue-800 text-center">총액: {data.총액 ? Number(data.총액).toLocaleString() + '원' : '-'}</h3>
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
        <div className="mt-4 flex justify-end gap-2">
        <button className="px-4 py-2 bg-gray-300 rounded" onClick={onClose}>닫기</button>
        </div>
      </div>
    </div>
    </div>
    );
};

export default LowestSumModal;