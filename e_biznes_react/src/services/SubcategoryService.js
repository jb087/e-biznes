const host = "http://localhost:9000/";
const apiPath = host + "api/";
const subcategoriesPath = apiPath + "subcategories";

export async function getSubcategories() {
    return fetch(subcategoriesPath, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json());
}
