import React, { useState } from 'react';
import { Brand } from '../types/models';

interface Props {
  data: Brand[];
  onDoubleClick: (brand: any) => void;
  onSelectedChange: (selectedIds: number[]) => void;
  isAdmin: boolean;
}

const BrandList: React.FC<Props> = ({ data, onDoubleClick, onSelectedChange, isAdmin }) => {

  const [selectedIds, setSelectedIds] = useState<number[]>([]);

  const handleCheckboxChange = (brandId: number) => {
    const updated = selectedIds.includes(brandId)
      ? selectedIds.filter(id => id !== brandId)
      : [...selectedIds, brandId];
    setSelectedIds(updated);
    onSelectedChange(updated); // 부모로 전달
  };

  const categoryNames = ["상의", "아우터", "바지", "스니커즈", "가방", "모자", "양말", "액세서리"];

  return (
    <div>
      <div className="text-right">단위: 원</div>
      <table className="w-full bg-white shadow-md rounded">
        <thead>
          <tr className="bg-gray-100 text-center">
          {/* 관리자 메뉴 진입 시에만 체크박스 활성화 */}
          {isAdmin && (
            <th className="p-2">선택</th>
          )}
            <th className="p-2">No</th>
            <th className="p-2">브랜드명</th>
            {categoryNames.map((category) => (
              <th key={category} className="p-4 text-right">
                {category}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((brand, idx) => (
            <tr
              key={idx}
              className="border-t hover:bg-gray-50 cursor-pointer"
              // 관리자 메뉴 진입 시에만 더블클릭 시 브랜드 수정 가능
              onDoubleClick={() => isAdmin? onDoubleClick(brand) : ''}
            >
            {isAdmin && (
              <td className="p-2 text-center">
                <input
                  type="checkbox"
                  checked={selectedIds.includes(brand.brandId!)}
                  onChange={() => handleCheckboxChange(brand.brandId!)}
                />
              </td>
            )}
              <td className="p-2 text-center">{idx + 1}</td>
              <td className="p-2 text-left">{brand.brandName}</td>
              {categoryNames.map((name) => (
                <td key={name} className="p-2 text-right">
                  {brand.categories[name] ? brand.categories[name].toLocaleString() : '-'}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      </div>
  );
};

export default BrandList;