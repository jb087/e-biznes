import React, {useContext} from 'react';
import {Form, Modal} from 'react-bootstrap'
import Button from "react-bootstrap/Button";
import {createBasket, createBasketWithUser} from "../services/BasketService";
import {createOrderedProduct} from "../services/OrderedProductsService";
import {createShippingInformation} from "../services/ShippingInformationService";
import {createOrder} from "../services/OrderService";
import {createPayment, deletePayment, finalizePayment} from "../services/PaymentService";
import {wasPaid} from "../services/PaymentServiceMock";
import ShippingInformationFormGroup from "./ShippingInformationFormGroup";
import {UserContext} from "../providers/UserProvider";

function PurchaseModal({show, onHide, orderedProducts}) {
    const {user} = useContext(UserContext);

    const finalizePurchase = async (event) => {
        event.preventDefault();
        event.persist();

        const basketId = await getBasketId();
        for (let i = 0; i < orderedProducts.length; i++) {
            await createOrderedProduct(getOrderedProduct(basketId, orderedProducts[i]));
        }

        const shippingInformationId = await createShippingInformation(getShippingInformation(event));
        const orderId = await createOrder(getOrder(basketId, shippingInformationId));

        const paymentId = await createPayment(orderId, onError);
        if (wasPaid()) {
            await finalizePayment(paymentId, onError);

            window.location.reload(false);
        } else {
            alert("Problem with payment. Probably You do not have enough money on account!");
            await deletePayment(paymentId, onError);
        }
    };

    const getBasketId = async () => {
        if (user) {
            return await createBasketWithUser(getBasket(), user)
                .then(response => response.text())
        } else {
            return await createBasket(getBasket());
        }
    };

    const getBasket = () => {
        return {
            id: "",
            userId: "",
            isBought: 0
        }
    };

    const getOrderedProduct = (basketId, product) => {
        return {
            id: "",
            basketId: basketId,
            productId: product.product.id,
            quantity: product.quantity
        }
    };

    const getShippingInformation = (event) => {
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

    const getOrder = (basketId, shippingInformationId) => {
        return {
            id: "",
            basketId: basketId,
            shippingInformationId: shippingInformationId,
            state: ""
        }
    };

    const onError = (errorMessage) => {
        alert(errorMessage);
        onHide();
    };

    return (
        <Modal size={"lg"} show={show} onHide={() => onHide()}>
            <Modal.Header closeButton>
                <h2>Purchase Process</h2>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={finalizePurchase}>
                    <div className="d-flex justify-content-between">
                        <div>
                            <h3>Shipping Information</h3>
                            <ShippingInformationFormGroup/>
                        </div>
                        <div>
                            {
                                orderedProducts && (
                                    <h4>
                                        Total price: {
                                        orderedProducts
                                            .reduce((sum, product) => sum + (product.quantity * product.product.price), 0)
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

export default PurchaseModal;
