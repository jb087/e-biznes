const host = "http://localhost:9000/";
const apiPath = host + "api/";
const productsPath = apiPath + "products";
const productPath = apiPath + "product/";

export async function getProducts() {
    return fetch(productsPath, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json());
}

export async function getProductById(productId) {
    return fetch(productPath + productId, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json());
}
