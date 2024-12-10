import { getTransactions } from "@/service/transaction-service";
import { ReactNode } from "react";

export default async function Transactions() {
  const transactions = await getTransactions();
  const transactionRows: ReactNode[] = [];

  transactions.forEach((transaction, index) => {
    const row =
      <tr key={index}>
        <th scope="row">{++index}</th>
        <td>{transaction.type}</td>
        <td>{transaction.uuid}</td>
        <td>{transaction.status}</td>
        <td>{transaction.customerEmail}</td>
        <td>{transaction.customerPhone}</td>
        <td>{transaction.referenceId}</td>
        <td>{transaction.amount && <>{transaction.amount}</>}</td>
      </tr>;
    transactionRows.push(row);
  });

  return (
    <main className="container">
      <h2 className="py-2">Transactions</h2>
      <table className="table">
        <thead>
          <tr>
            <th scope="col"></th>
            <th scope="col">Type</th>
            <th scope="col">UUID</th>
            <th scope="col">Status</th>
            <th scope="col">Email</th>
            <th scope="col">Phone</th>
            <th scope="col">Reference</th>
            <th scope="col">Amount</th>
          </tr>
        </thead>
        <tbody>
          {transactionRows}
        </tbody>
      </table>
    </main>
  );
}