import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {deleteProductById, getProducts} from "../../../../services/ProductService";
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";

function Products() {
    const {user} = useContext(UserContext);
    const [products, setProducts] = useState(null);

    useEffect(() => {
        getProducts()
            .then(productsFromRepo => setProducts(productsFromRepo))
    }, [setProducts]);

    function getNav() {
        return <nav className="navbar navbar-light bg-light">
            <Link to={"/"}>
                <img
                    src={logo}
                    alt="logo"
                    className="d-inline-block align-top logo mr-4"
                />
            </Link>
            <form className="form-inline">
                <Link to={"/adminPanel/product/create"}>
                    <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                        Create
                    </button>
                </Link>
                <Link to={"/adminPanel"}>
                    <button className="btn btn-outline-danger my-2 my-sm-0 mr-2">
                        Back
                    </button>
                </Link>
            </form>
        </nav>;
    }

    return (
        <div>
            {getNav()}
            {
                products && (
                    products.map(product => (
                        <div key={product.id}>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Id: {product.id}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>SubcategoryId: {product.subcategoryId}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Title: {product.title}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Price: {product.price}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Quantity: {product.quantity}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Date: {product.date}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Description: {product.description}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <button
                                    className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                    onClick={() => deleteProductById(product.id, user)}
                                >
                                    Delete
                                </button>
                                <Link to={"/adminPanel/product/edit/" + product.id}>
                                    <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                                        Edit
                                    </button>
                                </Link>
                            </div>
                            <br/>
                        </div>
                    ))
                )
            }
        </div>
    );
}

export default Products;
