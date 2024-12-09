import { getMerchant, getMerchants } from "@/service/merchant-service";
import { ReactNode } from "react";

export default async function Home({ params }: { params: { id: string; }; }) {
  const merchant = await getMerchant(params.id);

  return (
    <main className="container">
      <h2 className="py-2">Merchant</h2>
      <table className="table">
        <thead>
          <tr>
            <th scope="col">Name</th>
            <th scope="col">Description</th>
            <th scope="col">Email</th>
            <th scope="col">Status</th>
            <th scope="col">Total transaction sum</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{merchant.name}</td>
            <td>{merchant.description}</td>
            <td>{merchant.email}</td>
            <td>{merchant.status}</td>
            <td>{merchant.totalTransactionSum}</td>
          </tr>
        </tbody>
      </table>
    </main>
  );
}
