import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {Link, useParams} from "react-router-dom";
import {getBaskets} from "../../../../services/BasketService";
import {getShippingInformation} from "../../../../services/ShippingInformationService";
import {editOrder, getOrderById} from "../../../../services/OrderService";
import logo from "../../../../logo-e-biznes.png";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";

function OrderEdit() {
    const {user} = useContext(UserContext);
    const {orderId} = useParams();
    const [order, setOrder] = useState(null);
    const [baskets, setBaskets] = useState(null);
    const [shippingInformation, setShippingInformation] = useState(null);

    useEffect(() => {
        getOrderById(orderId)
            .then(orderFromRepo => setOrder(orderFromRepo));
        getBaskets()
            .then(basketsFromRepo => setBaskets(basketsFromRepo));
        getShippingInformation()
            .then(informationFromRepo => setShippingInformation(informationFromRepo));
    }, [orderId, setOrder, setBaskets, setShippingInformation]);

    function handleSubmit(event) {
        event.preventDefault();

        editOrder({
            id: orderId,
            basketId: event.target.elements.basketId.value,
            shippingInformationId: event.target.elements.shippingInformationId.value,
            state: order.state,
        }, user)
    }

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
                <Link to={"/adminPanel/orders"}>
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
                order && baskets && shippingInformation && (
                    <div>
                        <div className="row justify-content-center">
                            <h3>Edit Order</h3>
                        </div>
                        <div className="row justify-content-center">
                            <Form onSubmit={handleSubmit}>
                                <Form.Group controlId={"editOrder"}>
                                    <Form.Label>BasketId</Form.Label>
                                    <Form.Control
                                        required
                                        name={"basketId"}
                                        as={"select"}
                                        custom
                                    >
                                        {
                                            baskets.filter(basket => basket.isBought === 0)
                                                .map(basket => (
                                                    basket.id === order.basketId ?
                                                        <option key={basket.id}
                                                                value={basket.id}
                                                                selected={true}>{basket.id}</option> :
                                                        <option key={basket.id}
                                                                value={basket.id}>{basket.id}</option>
                                                ))
                                        }
                                    </Form.Control>
                                    <Form.Label>Shipping Information Id</Form.Label>
                                    <Form.Control
                                        required
                                        name={"shippingInformationId"}
                                        as={"select"}
                                        custom
                                    >
                                        {
                                            shippingInformation.map(information => (
                                                information.id === order.shippingInformationId ?
                                                    <option key={information.id}
                                                            value={information.id}
                                                            selected={true}>{information.id}</option> :
                                                    <option key={information.id}
                                                            value={information.id}>{information.id}</option>
                                            ))
                                        }
                                    </Form.Control>
                                </Form.Group>
                                <Button
                                    variant="primary"
                                    type="submit"
                                >
                                    Edit
                                </Button>
                            </Form>
                        </div>
                    </div>
                )
            }
        </div>
    );
}

export default OrderEdit;
