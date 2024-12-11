import { TransactionInfo } from "@/types/types";
import { getDataJSON } from "@/util/fetch-utils";
import { getAuthToken } from "./auth-service";

export async function getTransactions() {
  const token = await getAuthToken();
  const merchants: TransactionInfo[] = await getDataJSON(process.env.API + "transactions", token ? token : '');
  return merchants;
}