import {check400Status} from "../utils/RequestUtils";

const host = "http://localhost:9000/";
const apiPath = host + "api/";
const photosPath = apiPath + "photos";
const photosByProductIdPath = apiPath + "photos/";
const deletePhotoByIdPath = apiPath + "photo/{id}";
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

export async function getPhotosByProductId(productId) {
    return fetch(photosByProductIdPath + productId, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json());
}

export function deletePhotoById(photoId, user) {
    return fetch(deletePhotoByIdPath.replace("{id}", photoId), {
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
        .then(response => alert("Photo deleted!"))
        .then(response => window.location.reload(false))
}
