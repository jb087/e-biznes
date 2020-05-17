const host = "http://localhost:9000/";
const apiPath = host + "api/";
const opinionsByProductId = apiPath + "opinions/";

export async function getOpinionsByProductId(productId) {
    return fetch(opinionsByProductId + productId, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json());
}
