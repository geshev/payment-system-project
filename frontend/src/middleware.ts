import { NextRequest, NextResponse } from "next/server";
import { isAuthenticated } from "./service/auth-service";

const routes = [
  "/"
];

function isProtectedRoute(path: string): boolean {
  return routes.some((route) => path === route);
}

export async function middleware(request: NextRequest) {
  const currentPath = request.nextUrl.pathname;
  const isAuth = await isAuthenticated();

  if (isProtectedRoute(currentPath) && !isAuth) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  return NextResponse.next();
}