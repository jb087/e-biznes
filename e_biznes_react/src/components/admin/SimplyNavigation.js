import React from 'react';
import {Link} from "react-router-dom";
import logo from "../../logo-e-biznes.png";

function SimplyNavigation({backLink}) {
    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <Link to={"/"}>
                    <img
                        src={logo}
                        alt="logo"
                        className="d-inline-block align-top logo mr-4"
                    />
                </Link>
                <form className="form-inline">
                    <Link to={backLink}>
                        <button className="btn btn-outline-danger my-2 my-sm-0 mr-2">
                            Back
                        </button>
                    </Link>
                </form>
            </nav>
        </div>
    );
}

export default SimplyNavigation;
