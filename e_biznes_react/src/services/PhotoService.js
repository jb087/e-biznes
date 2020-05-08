const host = "http://localhost:9000/";
const apiPath = host + "api/";
const photosPath = apiPath + "photos";
export const photoJPGById = host + "photoJPG/";

export async function getPhotos() {
    return fetch(photosPath, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json());
}
