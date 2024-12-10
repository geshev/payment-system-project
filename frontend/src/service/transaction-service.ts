import { TransactionInfo } from "@/types/types";
import { getDataJSON } from "@/util/fetch-utils";
import { cookies } from "next/headers";

export async function getTransactions() {
  const cookieStore = await cookies();
  const token = cookieStore.get("auth-token")?.value;
  const merchants: TransactionInfo[] = await getDataJSON(process.env.API + "transactions", token ? token : '');
  return merchants;
}