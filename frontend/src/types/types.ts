export type LoginResponse = {
  token: string;
};

export enum Role {
  ADMIN,
  MERCHANT
}

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

export enum TransactionType {
  AUTHORIZE,
  CHARGE,
  REFUND,
  REVERSAL
}

export enum TransactionStatus {
  APPROVED,
  REVERSED,
  REFUNDED,
  ERROR
}

export type TransactionInfo = {
  type: TransactionType,
  uuid: string,
  status: TransactionStatus,
  customerEmail: string,
  customerPhone: string,
  referenceId: string,
  amount: number;
};