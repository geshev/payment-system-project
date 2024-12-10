import Merchants from "@/components/merchants";
import Transactions from "@/components/transactions";
import { Role } from "@/types/types";
import { getRole } from "@/util/cookies-utils";

export default async function Home() {
  const role = await getRole();

  return (
    <>
      {role === Role.ADMIN && <Merchants />}
      {role === Role.MERCHANT && <Transactions />}
    </>
  );
}
