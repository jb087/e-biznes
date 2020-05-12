const host = "http://localhost:9000/";
const apiPath = host + "api/";
const createOrderedProductPath = apiPath + "orderedProduct";

export async function createOrderedProduct(orderedProduct) {
    return fetch(createOrderedProductPath, {
        method: "POST",
        body: JSON.stringify(orderedProduct),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
}
