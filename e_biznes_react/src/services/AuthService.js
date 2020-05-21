const host = "http://localhost:9000/";

export async function authenticate(provider, queryParams) {
    return fetch(
        host + "authenticate/" + provider + "?" + queryParams,
        {
            method: "GET",
            credentials: 'include',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost:3000'
            }
        }
    )
        .then((response) => {
            if (response.status >= 400 && response.status < 600) {
                throw new Error('Bad response from server');
            }
            return response.json();
        })
        .then((fetchedUser) => {
            console.log(fetchedUser);
            return fetchedUser;
        })
        .catch(function (response) {
            console.log('Error on social auth');
            console.log(response);
        });
}
