const host = "http://localhost:9000/";
const apiPath = host + "api/";
const categoriesPath = apiPath + "categories";

export function getCategories() {
    return fetch(categoriesPath, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json());
}
