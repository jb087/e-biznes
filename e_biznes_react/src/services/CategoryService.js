import {check400Status} from "../utils/RequestUtils";

const host = "http://localhost:9000/";
const apiPath = host + "api/";
const categoriesPath = apiPath + "categories";
const createCategoryPath = apiPath + "category";
const deleteCategoryPath = apiPath + "category/{id}";
const categoryByIdPath = apiPath + "category/{id}";
const editCategoryPath = apiPath + "category/{id}";

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

export function createCategory(category, user) {
    return fetch(createCategoryPath, {
        method: "POST",
        credentials: 'include',
        body: JSON.stringify(category),
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000',
            'X-Auth-Token': user?.token
        }
    })
        .then(response => check400Status(response))
        .then(response => alert("Category created!"))
}

export function deleteCategory(categoryId, user) {
    return fetch(deleteCategoryPath.replace("{id}", categoryId), {
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
        .then(response => alert("Category deleted!"))
        .then(response => window.location.reload(false))
}

export function getCategoryById(categoryId) {
    return fetch(categoryByIdPath.replace("{id}", categoryId), {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => response.json());
}

export function editCategory(category, user) {
    return fetch(editCategoryPath.replace("{id}", category.id), {
        method: "PUT",
        credentials: 'include',
        body: JSON.stringify(category),
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
