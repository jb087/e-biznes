import {check400Status} from "../utils/RequestUtils";

const host = "http://localhost:9000/";
const apiPath = host + "api/";
const productsPath = apiPath + "products";
const productPath = apiPath + "product/";
const deleteProductByIdPath = apiPath + "product/{id}";
const createProductPath = apiPath + "product";
const editProductPath = apiPath + "product/{id}";

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

export function deleteProductById(productId, user) {
    return fetch(deleteProductByIdPath.replace("{id}", productId), {
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
        .then(response => alert("Product deleted!"))
        .then(response => window.location.reload(false))
}

export function createProduct(product, user) {
    return fetch(createProductPath, {
        method: "POST",
        body: JSON.stringify(product),
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Product created!"))
}

export function editProduct(product, user) {
    return fetch(editProductPath.replace("{id}", product.id), {
        method: "PUT",
        body: JSON.stringify(product),
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Product updated!"))
}
