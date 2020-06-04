import {check400Status} from "../utils/RequestUtils";

const host = "http://localhost:9000/";
const apiPath = host + "api/";
const basketsPath = apiPath + "baskets";
const createBasketPath = apiPath + "basket";
const deleteBasketByIdPath = apiPath + "basket/{id}";
const editBasketPath = apiPath + "basket/{id}";
const boughtBasketsByUserPath = apiPath + "basketByUser";

export function getBaskets() {
    return fetch(basketsPath, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json());
}

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

export async function createBasketWithUser(basket, user) {
    return fetch(createBasketPath, {
        method: "POST",
        credentials: 'include',
        body: JSON.stringify(basket),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
}

export function deleteBasketById(basketId, user) {
    return fetch(deleteBasketByIdPath.replace("{id}", basketId), {
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
        .then(response => alert("Basket deleted!"))
        .then(response => window.location.reload(false))
}

export function editBasket(basket, user) {
    return fetch(editBasketPath.replace("{id}", basket.id), {
        method: "PUT",
        body: JSON.stringify(basket),
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Basket edited!"))
        .then(response => window.location.reload(false))
}

export function getBoughtBasketsByUser(user) {
    return fetch(boughtBasketsByUserPath, {
        method: "GET",
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => response.json());
}
