import React, {Component} from 'react';
import {Form, Modal} from 'react-bootstrap'
import Button from "react-bootstrap/Button";
import {createBasket} from "../services/BasketService";
import {createOrderedProduct} from "../services/OrderedProductsService";
import {createShippingInformation} from "../services/ShippingInformationService";
import {createOrder} from "../services/OrderService";
import {createPayment, deletePayment, finalizePayment} from "../services/PaymentService";
import {wasPaid} from "../services/PaymentServiceMock";
import ShippingInformationFormGroup from "./ShippingInformationFormGroup";

class PurchaseModal extends Component {

    render() {
        return (
            <Modal size={"lg"} show={this.props.show} onHide={() => this.props.onHide()}>
                <Modal.Header closeButton>
                    <h2>Purchase Process</h2>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={this.finalizePurchase}>
                        <div className="d-flex justify-content-between">
                            <div>
                                <h3>Shipping Information</h3>
                                <ShippingInformationFormGroup/>
                            </div>
                            <div>
                                {
                                    this.props.orderedProducts && (
                                        <h4>
                                            Total price: {
                                            this.props.orderedProducts
                                                .reduce((sum, product) => sum + (product.product.price), 0)
                                        }
                                        </h4>
                                    )
                                }
                            </div>
                        </div>
                        <div className="row justify-content-center">
                            <Button
                                variant="primary"
                                type="submit"
                            >
                                Pay and Buy
                            </Button>
                        </div>
                    </Form>
                </Modal.Body>
            </Modal>
        );
    }

    finalizePurchase = async (event) => {
        event.preventDefault();
        event.persist();

        const basketId = await createBasket(this.getBasket());
        for (let i = 0; i < this.props.orderedProducts.length; i++) {
            await createOrderedProduct(this.getOrderedProduct(basketId, this.props.orderedProducts[i]));
        }

        const shippingInformationId = await createShippingInformation(this.getShippingInformation(event));
        const orderId = await createOrder(this.getOrder(basketId, shippingInformationId));

        const paymentId = await createPayment(orderId, this.onError);
        if (wasPaid()) {
            await finalizePayment(paymentId, this.onError);

            window.location.reload(false);
        } else {
            alert("Problem with payment. Probably You do not have enough money on account!");
            await deletePayment(paymentId, this.onError);
        }
    };

    getBasket = () => {
        return {
            id: "",
            isBought: 0
        }
    };

    getOrderedProduct = (basketId, product) => {
        return {
            id: "",
            basketId: basketId,
            productId: product.product.id,
            quantity: product.quantity
        }
    };

    getShippingInformation = (event) => {
        let elements = event.target.elements;
        return {
            id: "",
            firstName: elements.firstName.value,
            lastName: elements.lastName.value,
            email: elements.email.value,
            street: elements.street.value,
            houseNumber: elements.houseNumber.value,
            city: elements.city.value,
            zipCode: elements.zipCode.value
        }
    };

    getOrder = (basketId, shippingInformationId) => {
        return {
            id: "",
            basketId: basketId,
            shippingInformationId: shippingInformationId,
            state: ""
        }
    };

    onError = (errorMessage) => {
        alert(errorMessage);
        this.props.onHide();
    }
}

export default PurchaseModal;
