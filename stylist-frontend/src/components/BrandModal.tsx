import React, { useState, useEffect } from 'react';
import { Brand } from '../types/models';
import { insertBrand, updateBrand } from '../api/stylistApi';

interface BrandModalProps {
    isOpen: boolean;
    onClose: () => void;
    initialData?: Brand | null;
}

const categoryList = ['상의', '아우터', '바지', '스니커즈', '가방', '모자', '양말', '액세서리'];

const BrandModal: React.FC<BrandModalProps> = ({ isOpen, onClose, initialData }) => {
    
    const [brandName, setBrandName] = useState('');
    const [categories, setCategorys] = useState<{ [key: string]: number }>({});

    useEffect(() => {
        // props로 데이터 넘어온 경우
        if (initialData) {
            setBrandName(initialData.brandName);
            setCategorys(initialData.categories);
        } else {
            setBrandName('');
            setCategorys({});
        }
    }, [initialData]);

    // 카테고리별 금액 세팅
    const handleCategoryChange = (category: string, value: string) => {
        setCategorys({
            ...categories,
            [category]: Number(value),
        });
    };

    // 유효성 체크
    const validChk = () => {
        return () => {
          const specialCharRegex = /[^가-힣a-zA-Z0-9\s]/g; // 한글, 영문, 숫자, 공백 외
      
          if (!brandName) {
            alert('브랜드명을 입력해주세요.');
            return;
          }
          if (specialCharRegex.test(brandName)) {
            alert('특수문자는 입력할 수 없습니다.');
            setBrandName(''); // 브랜드명 초기화
            return;
          }

        // 카테고리 값 검사: 빈 값 또는 음수 체크
        for (const category of categoryList) {
            const value = categories[category];
            if (value === undefined || value === null ) {
                alert(`카테고리 [${category}]의 값을 입력해주세요.`);
                return;
            }
            if (isNaN(value) || value < 0) {
                alert(`카테고리 [${category}]의 값은 0 이상의 숫자여야 합니다.`);
                return;
            }
        }
            // 유효하면 등록/수정 실행
            handleSubmit(); 
        };
    };

    // 등록/수정 API 호출
    const handleSubmit = async () => {
        try {
            // payload에 담아서 전송
            const payload = {
                brandId: initialData?.brandId,
                name: brandName,
                categories: Object.entries(categories).map(([category, price]) => ({
                  category,
                  price,
                })),
              };
            const result = initialData ? await updateBrand(payload): await insertBrand(payload);
            if(result) alert(initialData ? '수정되었습니다.' : '등록되었습니다.');
        } catch (error) {
            console.error('에러 발생:', error);
            alert(initialData ? '수정 중 오류 발생' : '등록 중 오류 발생');
        }
        
        onClose();
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
        <div className="bg-white p-6 rounded shadow-lg w-[500px]">
            <h2 className="text-lg font-bold mb-4">{initialData ? '브랜드 정보 수정' : '신규 브랜드 등록'}</h2>
            <label className="block text-sm font-medium">브랜드명</label>
            <input
                className="w-full border p-2 mb-4"
                value={brandName}
                maxLength={15}
                onChange={(e) => setBrandName(e.target.value)}
            />
            <div className="grid grid-cols-2 gap-2">
            {categoryList.map((category) => (
                <div key={category}>
                <label className="block text-sm font-medium">{category}</label>
                <input
                    type="number"
                    className="w-full border p-1"
                    value={categories[category] ?? ''}
                    onChange={(e) => {
                        const value = e.target.value;
                        // 숫자 자릿수 제한: 15자리까지만 허용
                        if (value.length <= 15) {
                          handleCategoryChange(category, value);
                        }
                      }}
                    onWheel={(e) => e.currentTarget.blur()} // 스크롤 휠 입력 방지
                />
                </div>
            ))}
            </div>
            <div className="mt-4 flex justify-end gap-2">
            <button className="px-4 py-2 bg-gray-300 rounded" onClick={onClose}>취소</button>
            <button className="px-4 py-2 bg-blue-500 text-white rounded" onClick={validChk()}>
                {initialData ? '수정' : '등록'}
            </button>
            </div>
        </div>
        </div>
    );
};

export default BrandModal;