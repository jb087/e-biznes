import React, {useContext, useState} from 'react';
import logo from '../logo-e-biznes.png'
import '../css/NavigationBar.css'
import SocialLoginButton from "./SocialLoginButton";
import {UserContext} from "../providers/UserProvider";
import {signOut} from "../services/AuthService"
import {Link} from "react-router-dom";
import BoughtModal from "./BoughtModal";

export default function NavigationBar({hideBasket, showBasket}) {
    const {user, setUser} = useContext(UserContext);
    const [showModal, setShowModal] = useState(false);

    const handleSignOut = () => {
        signOut(user);
        setUser(null);
    };

    const hideBoughtModal = () => {
        setShowModal(false)
    };

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <div className={"Logo"} onClick={hideBasket}>
                    <img
                        src={logo}
                        alt="logo"
                        className="d-inline-block align-top logo mr-4"
                    />
                </div>
                <form className="form-inline">
                    {
                        user && (
                            <button
                                className="btn btn-outline-primary my-2 my-sm-0 mr-2"
                                type="button"
                                onClick={() => setShowModal(true)}
                            >
                                Bought
                            </button>
                        )
                    }
                    <button
                        className="btn btn-outline-primary my-2 my-sm-0 mr-2"
                        type="button"
                        onClick={() => showBasket()}
                    >
                        Basket
                    </button>
                    {
                        user && user.role === "Admin" && (
                            <Link to={"/adminPanel"}>
                                <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                                    Administration Panel
                                </button>
                            </Link>
                        )
                    }
                    {
                        user ? (
                            <button
                                className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                type="button"
                                onClick={handleSignOut}
                            >
                                Logout
                            </button>
                        ) : (
                            <div>
                                <SocialLoginButton provider={"google"} title={"Login with Google"}/>
                                <SocialLoginButton provider={"github"} title={"Login with GitHub"}/>
                            </div>
                        )
                    }
                </form>
            </nav>
            <BoughtModal
                show={showModal}
                onHide={hideBoughtModal}
            />
        </div>
    );
}
