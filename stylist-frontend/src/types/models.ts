export interface Brand {
    brandId?: number;
    brandName: string;
    categories: { [key: string]: number };
}