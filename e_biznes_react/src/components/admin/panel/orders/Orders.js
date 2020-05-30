import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {deleteOrderById, deliverOrderById, getOrders} from "../../../../services/OrderService";
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";

function Orders() {
    const {user} = useContext(UserContext);
    const [orders, setOrders] = useState(null);

    useEffect(() => {
        getOrders()
            .then(ordersFromRepo => setOrders(ordersFromRepo));
    }, [setOrders]);

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
                <Link to={"/adminPanel/order/create"}>
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
                orders && (
                    orders.map(order => (
                        <div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Id: {order.id}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Basket Id: {order.basketId}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Shipping Information Id: {order.shippingInformationId}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>State: {order.state}</h4>
                            </div>
                            <div className="row justify-content-center">
                                {
                                    (order.state === "CANCELLED" || order.state === "NOT_PAID") && (
                                        <Link to={"/adminPanel/order/edit/" + order.id}>
                                            <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                                                Edit
                                            </button>
                                        </Link>
                                    )
                                }
                                {
                                    order.state === "CANCELLED" && (
                                        <button
                                            className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                            onClick={() => deleteOrderById(order.id, user)}
                                        >
                                            Delete
                                        </button>
                                    )
                                }
                                {
                                    order.state === "PAID" && (
                                        <button
                                            className="btn btn-outline-primary my-2 my-sm-0 mr-2"
                                            onClick={() => deliverOrderById(order.id, user)}
                                        >
                                            Delivered
                                        </button>
                                    )
                                }
                            </div>
                            <br/>
                        </div>
                    ))
                )
            }
        </div>
    );
}

export default Orders;
