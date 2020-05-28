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
                    <Link to={"/adminPanel/subcategories"}>
                        <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                            Subcategories
                        </button>
                    </Link>
                    <Link to={"/adminPanel/products"}>
                        <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                            Products
                        </button>
                    </Link>
                    <Link to={"/adminPanel/opinions"}>
                        <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                            Opinions
                        </button>
                    </Link>
                    <Link to={"/adminPanel/photos"}>
                        <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                            Photos
                        </button>
                    </Link>
                    <Link to={"/adminPanel/baskets"}>
                        <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                            Baskets
                        </button>
                    </Link>
                    <Link to={"/adminPanel/orderedProducts"}>
                        <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                            Ordered Products
                        </button>
                    </Link>
                    <Link to={"/adminPanel/shippingInformation"}>
                        <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                            Shipping Information
                        </button>
                    </Link>
                    <Link to={"/adminPanel/orders"}>
                        <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                            Orders
                        </button>
                    </Link>
                </form>
            </nav>
        </div>
    );
}

export default AdminPanel;
