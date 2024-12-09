"use server";

import { updateMerchant } from "@/service/merchant-service";
import { MerchantUpdate } from "@/types/types";
import { redirect } from "next/navigation";

export async function updateMerchantAction(formData: FormData) {
  const obj: any = {};
  formData.forEach((value, key) => {
    obj[key] = value;
  });

  const update: MerchantUpdate = {
    name: obj.name,
    description: obj.description,
    email: obj.email,
    status: obj.status
  };

  await updateMerchant(obj['name-id'], update);
  redirect("/");
}