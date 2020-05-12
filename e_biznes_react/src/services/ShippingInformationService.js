const host = "http://localhost:9000/";
const apiPath = host + "api/";
const shippingInformationPath = apiPath + "shippingInformation";

export async function createShippingInformation(shippingInformation) {
    return fetch(shippingInformationPath, {
        method: "POST",
        body: JSON.stringify(shippingInformation),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.text())
}
