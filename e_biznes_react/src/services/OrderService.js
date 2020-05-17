const host = "http://localhost:9000/";
const apiPath = host + "api/";
const createOrderPath = apiPath + "order";

export async function createOrder(order) {
    return fetch(createOrderPath, {
        method: "POST",
        body: JSON.stringify(order),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.text())
}
