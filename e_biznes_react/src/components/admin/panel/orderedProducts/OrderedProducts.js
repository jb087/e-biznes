import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {getBaskets} from "../../../../services/BasketService";
import {deleteOrderedProductById, getOrderedProducts} from "../../../../services/OrderedProductsService";
import {Link} from "react-router-dom";
import SimplyNavigation from "../../SimplyNavigation";

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

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel"} createLink={"/adminPanel/orderedProduct/create"}/>
            {
                baskets && orderedProducts && (
                    orderedProducts.map(orderedProduct => (
                        <div key={orderedProduct.id}>
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
