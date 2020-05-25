import {check400Status} from "../utils/RequestUtils";

const host = "http://localhost:9000/";
const apiPath = host + "api/";
const opinionsByProductIdPath = apiPath + "opinions/";
const createOpinionPath = apiPath + "opinion";

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
