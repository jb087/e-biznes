const host = "http://localhost:9000/";
const apiPath = host + "api/";
const createPaymentPath = apiPath + "payment/{orderId}";

export async function createPayment(orderId, errorCallback) {
    return fetch(createPaymentPath.replace("{orderId}", orderId), {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'http://localhost:3000'
        }
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            }
            response.text().then(errorMessage => errorCallback(errorMessage));
        })
}
