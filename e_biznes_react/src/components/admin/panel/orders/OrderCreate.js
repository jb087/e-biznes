import React, {useEffect, useState} from 'react';
import {getBaskets} from "../../../../services/BasketService";
import {getShippingInformation} from "../../../../services/ShippingInformationService";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {check400Status} from "../../../../utils/RequestUtils";
import {createOrder} from "../../../../services/OrderService";
import SimplyNavigation from "../../SimplyNavigation";

function OrderCreate() {
    const [baskets, setBaskets] = useState(null);
    const [shippingInformation, setShippingInformation] = useState(null);

    useEffect(() => {
        getBaskets()
            .then(basketsFromRepo => setBaskets(basketsFromRepo));
        getShippingInformation()
            .then(informationFromRepo => setShippingInformation(informationFromRepo));
    }, [setBaskets, setShippingInformation]);

    function handleSubmit(event) {
        event.preventDefault();

        createOrder({
            id: "",
            basketId: event.target.elements.basketId.value,
            shippingInformationId: event.target.elements.shippingInformationId.value,
            state: "",
        })
            .then(response => check400Status(response))
            .then(response => alert("Order created!"));
    }

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel/orders"}/>
            {
                baskets && shippingInformation && (
                    <div>
                        <div className="row justify-content-center">
                            <h3>Create Order</h3>
                        </div>
                        <div className="row justify-content-center">
                            <Form onSubmit={handleSubmit}>
                                <Form.Group controlId={"createOrder"}>
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
                                    Create
                                </Button>
                            </Form>
                        </div>
                    </div>
                )
            }
        </div>
    );
}

export default OrderCreate;
