import { cookies } from "next/headers";
import { jwtDecode, JwtPayload } from "jwt-decode";
import { Role } from "@/types/types";

interface RoleJwtPayload extends JwtPayload {
  role: string;
}

export async function getRole() {
  const cookie = (await cookies()).get("auth-token");
  const token = cookie ? cookie.value : '';
  const decoded: RoleJwtPayload = jwtDecode(token);
  return Role[decoded.role as keyof typeof Role];
}