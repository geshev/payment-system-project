import { notFound, redirect } from "next/navigation";

async function getData(request: string, auth: string) {
  const response = await executeFetch(request, {
    headers: {
      'Authorization': `Bearer ${auth}`
    },
    cache: 'no-store'
  });
  return response;
}

export async function getDataJSON(request: string, auth: string) {
  const response = await getData(request, auth);
  return response.json();
}

async function postData(request: string, data: any) {
  const response = await executeFetch(request, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
    cache: 'no-store'
  });
  return response;
}

export async function postDataJSONResponse(request: string, data: any) {
  const response = await postData(request, data);
  return response.json();
}

export async function putData(request: string, auth: string, data: any) {
  const response = await executeFetch(request, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${auth}`
    },
    body: JSON.stringify(data),
    cache: 'no-store'
  });
  return response;
}

async function executeFetch(input: string, init: RequestInit) {
  const response = await fetch(input, init);
  if (!response.ok) {
    if (response.status == 404) {
      notFound();
    } else if (response.status == 401) {
      redirect("/logout");
    } else {
      redirect("/error");
    }
  }
  return response;
}