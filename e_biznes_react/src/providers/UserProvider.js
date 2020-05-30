import React, { createContext, useState, useCallback } from 'react';

export const UserContext = createContext(null);

export default function UserProvider({ children }) {
    const [user, setUser] = useState(() => {
        const userString = window.localStorage.getItem("e-biznes-user");

        return userString ? JSON.parse(userString) : null;
    });
    const handleSetUser = useCallback(
        (handledUser) => {
            if (handledUser) {
                window.localStorage.setItem("e-biznes-user", JSON.stringify(handledUser));
            } else {
                window.localStorage.removeItem("e-biznes-user");
            }

            setUser(handledUser);
        },
        [setUser]
    );

    return (
        <UserContext.Provider value={{ user, setUser: handleSetUser }}>
            {children}
        </UserContext.Provider>
    );
}
