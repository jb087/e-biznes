import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {getBaskets} from "../../../../services/BasketService";
import {deleteOrderedProductById, getOrderedProducts} from "../../../../services/OrderedProductsService";
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";

function OrderedProducts() {
    const {user} = useContext(UserContext);
    const [baskets, setBaskets] = useState(null);
    const [orderedProducts, setOrderedProducts] = useState(null);

    useEffect(() => {
        getBaskets()
            .then(basketsFromRepo => setBaskets(basketsFromRepo));
        getOrderedProducts()
            .then(orderedProductsFromRepo => setOrderedProducts(orderedProductsFromRepo));
    }, [setBaskets, setOrderedProducts]);

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
                <Link to={"/adminPanel/orderedProduct/create"}>
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
                baskets && orderedProducts && (
                    orderedProducts.map(orderedProduct => (
                        <div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Id: {orderedProduct.id}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>BasketId: {orderedProduct.basketId}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>ProductId: {orderedProduct.productId}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Quantity: {orderedProduct.quantity}</h4>
                            </div>
                            {
                                baskets.find(basket => basket.id === orderedProduct.basketId).isBought === 0 && (
                                    <div className="row justify-content-center">
                                        <button
                                            className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                            onClick={() => deleteOrderedProductById(orderedProduct.id, user)}
                                        >
                                            Delete
                                        </button>
                                        <Link to={"/adminPanel/orderedProduct/edit/" + orderedProduct.id}>
                                            <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                                                Edit
                                            </button>
                                        </Link>
                                    </div>
                                )
                            }
                            <br/>
                        </div>
                    ))
                )
            }
        </div>
    );
}

export default OrderedProducts;
