import { removeAuthentication } from "@/service/auth-service";
import { redirect } from "next/navigation";

export async function GET() {
  await removeAuthentication();
  redirect("/login");
}