import type { Metadata } from "next";

import 'bootstrap/dist/css/bootstrap.css';
import "./globals.css";

import Header from "@/components/header";

export const metadata: Metadata = {
  title: "Payment system",
  description: "Payment system project",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode; }>) {
  return (
    <html lang="en">
      <body>
        <Header />
        {children}
      </body>
    </html>
  );
}
