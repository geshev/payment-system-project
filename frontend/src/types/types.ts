export type LoginResponse = {
  token: string;
};

export enum MerchantStatus {
  ACTIVE,
  INACTIVE
}

export type MerchantInfo = {
  name: string,
  description: string,
  email: string,
  status: MerchantStatus,
  totalTransactionSum: number;
};

export type MerchantUpdate = {
  name: string,
  description: string,
  email: string,
  status: MerchantStatus,
};