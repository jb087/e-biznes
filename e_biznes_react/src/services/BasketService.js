const host = "http://localhost:9000/";
const apiPath = host + "api/";
const createBasketPath = apiPath + "basket";

export async function createBasket(basket) {
    return fetch(createBasketPath, {
        method: "POST",
        body: JSON.stringify(basket),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.text())
}
