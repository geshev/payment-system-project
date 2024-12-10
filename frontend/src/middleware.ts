import { NextRequest, NextResponse } from "next/server";
import { isAuthenticated } from "./service/auth-service";

const protectedRoutes = [
  "/merchants"
];

function isProtectedRoute(path: string): boolean {
  return path === "/" || protectedRoutes.some((route) => path.startsWith(route));
}

export async function middleware(request: NextRequest) {
  const currentPath = request.nextUrl.pathname;
  const isAuth = await isAuthenticated();

  if (isProtectedRoute(currentPath) && !isAuth) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  return NextResponse.next();
}