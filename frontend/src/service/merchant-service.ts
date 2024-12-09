import { MerchantInfo, MerchantUpdate } from "@/types/types";
import { getDataJSON, putData } from "@/util/fetch-utils";
import { cookies } from "next/headers";

export async function getMerchants() {
  const cookieStore = await cookies();
  const token = cookieStore.get("auth-token")?.value;
  const merchants: MerchantInfo[] = await getDataJSON(process.env.API + "merchants", token ? token : '');
  return merchants;
}

export async function getMerchant(name: string) {
  const cookieStore = await cookies();
  const token = cookieStore.get("auth-token")?.value;
  const merchant: MerchantInfo = await getDataJSON(process.env.API + `merchants/${name}`, token ? token : '');
  return merchant;
}

export async function updateMerchant(name: string, update: MerchantUpdate) {
  const cookieStore = await cookies();
  const token = cookieStore.get("auth-token")?.value;
  await putData(process.env.API + `merchants/${name}`, token ? token : '', update);
}