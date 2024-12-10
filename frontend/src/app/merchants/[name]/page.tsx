import { updateMerchantAction } from "@/actions/merchants";
import { getMerchant } from "@/service/merchant-service";
import { MerchantStatus } from "@/types/types";

export default async function Merchant({ params }: { params: { name: string; }; }) {
  const merchant = await getMerchant(params.name);

  return (
    <main className="container">
      <h2 className="py-2">Merchant</h2>
      <form action={updateMerchantAction}>
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
              <td>
                <input type="hidden" name="name-id" defaultValue={params.name} />
                <input type="text" name="name" defaultValue={merchant.name} className="w-100" />
              </td>
              <td>
                <input type="text" name="description" defaultValue={merchant.description} className="w-100" />
              </td>
              <td>
                <input type="text" name="email" defaultValue={merchant.email} className="w-100" />
              </td>
              <td>
                <select name="status" defaultValue={merchant.status}>
                  {Object.keys(MerchantStatus).filter((key) => isNaN(Number(key))).map((status) => (
                    <option key={status} value={status}>
                      {status}
                    </option>
                  ))}
                </select>
              </td>
              <td>{merchant.totalTransactionSum}</td>
            </tr>
          </tbody>
        </table>
        <button type="submit" className="main-color text-white btn btn-primary border-0">Update</button>
      </form>
    </main>
  );
}
