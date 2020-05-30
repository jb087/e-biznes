export const check400Status = (response) => {
    if (response.status >= 400 && response.status < 500) {
        throw new Error('Bad response from server');
    }
    return response;
};
