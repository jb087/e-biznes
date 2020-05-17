const host = "http://localhost:9000/";
const apiPath = host + "api/";
const createPaymentPath = apiPath + "payment/{orderId}";
const finalizePaymentPath = apiPath + "payment/{paymentId}";
const deletePaymentPath = finalizePaymentPath;

export async function createPayment(orderId, errorCallback) {
    return fetch(createPaymentPath.replace("{orderId}", orderId), {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost:3000'
        }
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            }
            response.text().then(errorMessage => errorCallback(errorMessage));
        })
}

export async function finalizePayment(paymentId, errorCallback) {
    return fetch(finalizePaymentPath.replace("{paymentId}", paymentId), {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost:3000'
        }
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            }
            response.text().then(errorMessage => errorCallback(errorMessage));
        })
}

export async function deletePayment(paymentId, errorCallback) {
    return fetch(deletePaymentPath.replace("{paymentId}", paymentId), {
        method: "DELETE",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost:3000'
        }
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            }
            response.text().then(errorMessage => errorCallback(errorMessage));
        })
}
