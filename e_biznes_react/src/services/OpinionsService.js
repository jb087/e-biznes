import {check400Status} from "../utils/RequestUtils";

const host = "http://localhost:9000/";
const apiPath = host + "api/";
const opinionsPath = apiPath + "opinions";
const opinionsByProductIdPath = apiPath + "opinions/";
const createOpinionPath = apiPath + "opinion";
const deleteOpinionByIdPath = apiPath + "opinion/{id}";

export async function getOpinions() {
    return fetch(opinionsPath, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => check400Status(response))
        .then(response => response.json());
}

export async function getOpinionsByProductId(productId) {
    return fetch(opinionsByProductIdPath + productId, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => check400Status(response))
        .then(response => response.json());
}

export function createOpinion(opinion) {
    return fetch(createOpinionPath, {
        method: "POST",
        body: JSON.stringify(opinion),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Opinion added!"))
}

export function deleteOpinionById(opinionId, user) {
    return fetch(deleteOpinionByIdPath.replace("{id}", opinionId), {
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
        .then(response => alert("Opinion deleted!"))
        .then(response => window.location.reload(false))
}
