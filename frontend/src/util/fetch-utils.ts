async function getData(request: string) {
  let response;
  try {
    response = await fetch(request, { cache: 'no-store' });
    if (!response.ok) {
      throw Error("Fetch response: " + response.status);
    }
  } catch (e) {
    throw Error("Fetch error: " + e);
  }
  return response;
}

async function postData(request: string, data: any) {
  let response;
  try {
    response = await fetch(request, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
      cache: 'no-store'
    });
    if (!response.ok) {
      throw Error("Fetch response: " + response.status);
    }
  } catch (e) {
    throw Error("Fetch error: " + e);
  }
  return response;
}

export async function postDataJSONResponse(request: string, data: any) {
  const response = await postData(request, data);
  return response.json();
}