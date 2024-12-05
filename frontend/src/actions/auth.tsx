"use server";

import { authenticate, removeAuthentication } from "@/service/auth-service";
import { redirect } from "next/navigation";

export async function loginAction(formData: FormData) {
  await authenticate(formData.get("username"), formData.get("password"));
  redirect("/");
}

export async function logoutAction() {
  await removeAuthentication();
  redirect("/login");
}