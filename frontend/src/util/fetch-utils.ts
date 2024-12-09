import { notFound, redirect } from "next/navigation";

async function getData(request: string, auth: string) {
  const response = await fetch(request, {
    headers: {
      'Authorization': `Bearer ${auth}`
    },
    cache: 'no-store'
  });
  if (!response.ok) {
    if (response.status == 404) {
      notFound();
    } else {
      redirect("/error");
    }
  }
  return response;
}

export async function getDataJSON(request: string, auth: string) {
  const response = await getData(request, auth);
  return response.json();
}

async function postData(request: string, data: any) {
  const response = await fetch(request, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
    cache: 'no-store'
  });
  if (!response.ok) {
    if (response.status == 404) {
      notFound();
    } else {
      redirect("/error");
    }
  }
  return response;
}

export async function postDataJSONResponse(request: string, data: any) {
  const response = await postData(request, data);
  return response.json();
}

export async function putData(request: string, auth: string, data: any) {
  const response = await fetch(request, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${auth}`
    },
    body: JSON.stringify(data),
    cache: 'no-store'
  });
  if (!response.ok) {
    if (response.status == 404) {
      notFound();
    } else {
      redirect("/error");
    }
  }
  return response;
}