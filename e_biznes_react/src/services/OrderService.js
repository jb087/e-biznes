import {check400Status} from "../utils/RequestUtils";

const host = "http://localhost:9000/";
const apiPath = host + "api/";
const ordersPath = apiPath + "orders";
const orderPath = apiPath + "order";
const orderDeliverPath = apiPath + "orderDeliver";

export function getOrders() {
    return fetch(ordersPath, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost:3000'
        }
    })
        .then(response => response.json())
}

export function getOrderById(orderId) {
    return fetch(orderPath + "/" + orderId, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost:3000'
        }
    })
        .then(response => response.json())
}

export async function createOrder(order) {
    return fetch(orderPath, {
        method: "POST",
        body: JSON.stringify(order),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost:3000'
        }
    })
        .then(response => response.text())
}

export function deleteOrderById(orderId, user) {
    return fetch(orderPath + "/" + orderId, {
        method: "DELETE",
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Order deleted!"))
        .then(response => window.location.reload(false))
}

export function deliverOrderById(orderId, user) {
    return fetch(orderDeliverPath + "/" + orderId, {
        method: "PUT",
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Order delivered!"))
        .then(response => window.location.reload(false))
}

export function editOrder(order, user) {
    return fetch(orderPath + "/" + order.id, {
        method: "PUT",
        body: JSON.stringify(order),
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Order updated!"))
}
