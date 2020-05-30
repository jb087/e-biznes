import React, {useContext} from 'react';
import {UserContext} from "../../../providers/UserProvider";

function AuthAdminCheck({children}) {
    const {user} = useContext(UserContext);

    return (
        <div>
            {
                user && user.role === "Admin" ? (
                    children
                ) : (
                    <div className="row justify-content-center">
                        <h3>Permission Denied!</h3>
                    </div>
                )
            }
        </div>
    );
}

export default AuthAdminCheck;
