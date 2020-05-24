import React from 'react';
import logo from "../../../logo-e-biznes.png";
import {Link} from "react-router-dom";

function AdminPanel() {

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
                    <Link to={"/adminPanel/categories"}>
                        <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                            Categories
                        </button>
                    </Link>
                </form>
            </nav>
        </div>
    );
}

export default AdminPanel;
