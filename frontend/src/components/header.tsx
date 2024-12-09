import { logoutAction } from "@/actions/auth";
import { isAuthenticated } from "@/service/auth-service";

export default async function Header() {
  const isAuth = await isAuthenticated();

  return (
    <div className="main-color text-white w-100">
      <div className="container h-100">
        <div className="h-100 d-flex p-2 justify-content-between align-items-center ">
          <a href="/" className="text-decoration-none text-white">PAYMENT SYSTEM</a>
          {isAuth &&
            <form action={logoutAction}>
              <button type="submit" className="px-4 rounded bg-primary border-0 text-white">Log out</button>
            </form>
          }
        </div>
      </div>
    </div>
  );
}