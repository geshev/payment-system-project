import { loginAction } from "@/actions/auth";

export default function Login() {
  return (
    <main className="container pt-3 d-flex justify-content-center">
      <form action={loginAction} className="w-25">
        <div className="d-flex flex-column gap-3">
          <input id="username" name="username" placeholder="username" />
          <input id="password" name="password" type="password" placeholder="password" />
          <button type="submit" className="main-color text-white btn btn-primary border-0">Log in</button>
        </div>
      </form>
    </main>
  );
}