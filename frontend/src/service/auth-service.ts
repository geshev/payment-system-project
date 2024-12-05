import { LoginResponse } from "@/types/types";
import { postDataJSONResponse } from "@/util/fetch-utils";
import { cookies } from "next/headers";

const config = {
  maxAge: 60 * 60 * 24, // 1 day
  path: "/",
  domain: process.env.HOST ?? "localhost",
  httpOnly: true,
  secure: process.env.NODE_ENV === "production",
};

export async function authenticate(username: any, password: any) {
  try {
    const response: LoginResponse = await postDataJSONResponse(process.env.API + "auth/token",
      { "username": username, "password": password });

    const cookieStore = await cookies();
    cookieStore.set("auth-token", response.token, config);
  } catch (e) { }
}

export async function isAuthenticated() {
  const cookieStore = await cookies();
  return cookieStore.has("auth-token");
}

export async function removeAuthentication() {
  const cookieStore = await cookies();
  cookieStore.delete("auth-token");
}