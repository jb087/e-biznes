import {check400Status} from "../utils/RequestUtils";

const host = "http://localhost:9000/";
const apiPath = host + "api/";
const shippingInformationPath = apiPath + "shippingInformation";

export function getShippingInformation() {
    return fetch(shippingInformationPath, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json())
}

export function getShippingInformationById(shippingInformationId) {
    return fetch(shippingInformationPath + "/" + shippingInformationId, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json())
}

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

export function deleteShippingInformationById(shippingInformationId, user) {
    return fetch(shippingInformationPath + "/" + shippingInformationId, {
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
        .then(response => alert("Shipping Information deleted!"))
        .then(response => window.location.reload(false))
}

export function editShippingInformation(shippingInformation, user) {
    return fetch(shippingInformationPath + "/" + shippingInformation.id, {
        method: "PUT",
        body: JSON.stringify(shippingInformation),
        credentials: 'include',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Shipping Information updated!"))
}
