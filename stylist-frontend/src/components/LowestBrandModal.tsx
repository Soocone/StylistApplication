import React from 'react';

interface LowestBrandModalProps {
    onClose: () => void;
    data: any
}

const LowestBrandModal: React.FC<LowestBrandModalProps> = ({ onClose, data }) => {

    return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
    <div className="bg-white p-4 rounded shadow-md mb-6">
    <h2 className="text-xl font-semibold mb-3">💥 카테고리별로 최저가격에 판매하는 브랜드예요.</h2>
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
                {Object.entries(data)
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
                    {typeof data?.total === 'number' ? data.total.toLocaleString() + '원' : '-'}
                </td>
                </tr>
            </tbody>
            </table>
        </div>
        <div className="mt-4 flex justify-end gap-2">
          <button className="px-4 py-2 bg-gray-300 rounded" onClick={onClose}>닫기</button>
        </div>
    </div>
    </div>
    );
};

export default LowestBrandModal;