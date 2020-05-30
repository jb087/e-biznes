import {check400Status} from "../utils/RequestUtils";

const host = "http://localhost:9000/";
const apiPath = host + "api/";
const orderedProductsPath = apiPath + "orderedProducts";
const orderedProductByIdPath = apiPath + "orderedProduct/{id}";
const createOrderedProductPath = apiPath + "orderedProduct";
const deleteOrderedProductByIdPath = apiPath + "orderedProduct/{id}";
const editOrderedProductByIdPath = apiPath + "orderedProduct/{id}";

export function getOrderedProducts() {
    return fetch(orderedProductsPath, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json());
}

export function getOrderedProductById(orderedProductId) {
    return fetch(orderedProductByIdPath.replace("{id}", orderedProductId), {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json());
}

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

export function deleteOrderedProductById(orderedProductId, user) {
    return fetch(deleteOrderedProductByIdPath.replace("{id}", orderedProductId), {
        method: "DELETE",
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Ordered Product deleted!"))
        .then(response => window.location.reload(false))
}

export function editOrderedProduct(orderedProduct, user) {
    return fetch(editOrderedProductByIdPath.replace("{id}", orderedProduct.id), {
        method: "PUT",
        body: JSON.stringify(orderedProduct),
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Ordered Product updated!"))
}
