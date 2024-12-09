import { getMerchants } from "@/service/merchant-service";
import { ReactNode } from "react";

export default async function Home() {
  const merchants = await getMerchants();
  const merchantRows: ReactNode[] = [];

  merchants.forEach((merchant, index) => {
    const row =
      <tr key={index}>
        <th scope="row">{++index}</th>
        <td><a href={`merchants/${merchant.name}`}>{merchant.name}</a></td>
        <td>{merchant.description}</td>
        <td>{merchant.email}</td>
        <td>{merchant.status}</td>
        <td>{merchant.totalTransactionSum}</td>
      </tr>;
    merchantRows.push(row);
  });

  return (
    <main className="container">
      <h2 className="py-2">Merchants</h2>
      <table className="table">
        <thead>
          <tr>
            <th scope="col"></th>
            <th scope="col">Name</th>
            <th scope="col">Description</th>
            <th scope="col">Email</th>
            <th scope="col">Status</th>
            <th scope="col">Total transaction sum</th>
          </tr>
        </thead>
        <tbody>
          {merchantRows}
        </tbody>
      </table>
    </main>
  );
}
