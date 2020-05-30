import React, {useEffect, useState} from 'react';
import {getOrders} from "../../../../services/OrderService";
import {createPayment, getPayments} from "../../../../services/PaymentService";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import SimplyNavigation from "../../SimplyNavigation";

function PaymentCreate() {
    const [orders, setOrders] = useState(null);
    const [paymentOrderIds, setPaymentOrderIds] = useState(null);

    useEffect(() => {
        getOrders()
            .then(ordersFromRepo => setOrders(ordersFromRepo));
        getPayments()
            .then(paymentsFromRepo => setPaymentOrderIds(paymentsFromRepo.map(payment => payment.orderId)));
    }, [setOrders, setPaymentOrderIds]);

    function handleSubmit(event) {
        event.preventDefault();

        createPayment(event.target.elements.orderId.value, () => alert("Problem with payment creation!"))
            .then(response => alert("Payment created!"));
    }

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel/payments"}/>
            {
                orders && paymentOrderIds && (
                    <div>
                        <div className="row justify-content-center">
                            <h3>Create Payment</h3>
                        </div>
                        <div className="row justify-content-center">
                            <Form onSubmit={handleSubmit}>
                                <Form.Group controlId={"createPayment"}>
                                    <Form.Label>OrderId</Form.Label>
                                    <Form.Control
                                        required
                                        name={"orderId"}
                                        as={"select"}
                                        custom
                                    >
                                        {
                                            orders.filter(order => !paymentOrderIds.includes(order.id))
                                                .map(order => (
                                                    <option key={order.id}
                                                            value={order.id}>{order.id}</option>
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

export default PaymentCreate;
