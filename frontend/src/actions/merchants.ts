"use server";

import { deleteMerchant, updateMerchant } from "@/service/merchant-service";
import { MerchantUpdate } from "@/types/types";
import { redirect } from "next/navigation";

export async function updateMerchantAction(formData: FormData) {
  const obj = formDataToObject(formData);

  const update: MerchantUpdate = {
    name: obj.name,
    description: obj.description,
    email: obj.email,
    status: obj.status
  };

  await updateMerchant(obj['name-id'], update);
  redirect("/");
}

export async function deleteMerchantAction(formData: FormData) {
  const obj = formDataToObject(formData);
  await deleteMerchant(obj["name-id"]);
  redirect("/");
}

function formDataToObject(formData: FormData) {
  const obj: any = {};
  formData.forEach((value, key) => {
    obj[key] = value;
  });
  return obj;
}