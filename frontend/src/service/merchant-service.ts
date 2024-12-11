import { MerchantInfo, MerchantUpdate } from "@/types/types";
import { deleteData, getDataJSON, putData } from "@/util/fetch-utils";
import { getAuthToken } from "./auth-service";

export async function getMerchants() {
  const token = await getAuthToken();
  const merchants: MerchantInfo[] = await getDataJSON(process.env.API + "merchants", token ? token : '');
  return merchants;
}

export async function getMerchant(name: string) {
  const token = await getAuthToken();
  const merchant: MerchantInfo = await getDataJSON(process.env.API + `merchants/${name}`, token ? token : '');
  return merchant;
}

export async function updateMerchant(name: string, update: MerchantUpdate) {
  const token = await getAuthToken();
  await putData(process.env.API + `merchants/${name}`, token ? token : '', update);
}

export async function deleteMerchant(name: string) {
  const token = await getAuthToken();
  await deleteData(process.env.API + `merchants/${name}`, token ? token : '');
}