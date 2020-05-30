import {check400Status} from "../utils/RequestUtils";

const host = "http://localhost:9000/";
const apiPath = host + "api/";
const subcategoriesPath = apiPath + "subcategories";
const deleteSubcategoryPath = apiPath + "subcategory/{id}";
const createSubcategoryPath = apiPath + "subcategory";
const subcategoryByIdPath = apiPath + "subcategory/{id}";
const editSubcategoryPath = apiPath + "subcategory/{id}";

export function getSubcategories() {
    return fetch(subcategoriesPath, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost:3000'
        }
    })
        .then(response => response.json());
}

export function deleteSubcategory(subcategoryId, user) {
    return fetch(deleteSubcategoryPath.replace("{id}", subcategoryId), {
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
        .then(response => alert("Subcategory deleted!"))
        .then(response => window.location.reload(false))
}

export function createSubcategory(subcategory, user) {
    return fetch(createSubcategoryPath, {
        method: "POST",
        credentials: 'include',
        body: JSON.stringify(subcategory),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Subcategory created!"))
}

export function getSubcategoryById(subcategoryId) {
    return fetch(subcategoryByIdPath.replace("{id}", subcategoryId), {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost:3000'
        }
    })
        .then(response => response.json());
}

export function editSubcategory(subcategory, user) {
    return fetch(editSubcategoryPath.replace("{id}", subcategory.id), {
        method: "PUT",
        credentials: 'include',
        body: JSON.stringify(subcategory),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Category updated!"))
}
