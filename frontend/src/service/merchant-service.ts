import { MerchantInfo } from "@/types/types";
import { getDataJSON } from "@/util/fetch-utils";
import { cookies } from "next/headers";

export async function getMerchants() {
  const cookieStore = await cookies();
  const token = cookieStore.get("auth-token")?.value;
  const merchants: MerchantInfo[] = await getDataJSON(process.env.API + "merchants", token ? token : '');
  return merchants;
}