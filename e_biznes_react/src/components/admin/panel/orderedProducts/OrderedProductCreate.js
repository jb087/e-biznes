import React, {useEffect, useState} from 'react';
import {getBaskets} from "../../../../services/BasketService";
import {createOrderedProduct} from "../../../../services/OrderedProductsService";
import {getProducts} from "../../../../services/ProductService";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {check400Status} from "../../../../utils/RequestUtils";
import SimplyNavigation from "../../SimplyNavigation";

function OrderedProductCreate() {
    const [baskets, setBaskets] = useState(null);
    const [products, setProducts] = useState(null);

    useEffect(() => {
        getBaskets()
            .then(basketsFromRepo => setBaskets(basketsFromRepo));
        getProducts()
            .then(productsFromRepo => setProducts(productsFromRepo))
    }, [setBaskets, setProducts]);

    function handleSubmit(event) {
        event.preventDefault();

        createOrderedProduct({
            id: "",
            basketId: event.target.elements.basketId.value,
            productId: event.target.elements.productId.value,
            quantity: parseInt(event.target.elements.quantity.value),
        })
            .then(response => check400Status(response))
            .then(response => alert("Ordered Product created!"));
    }

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel/orderedProducts"}/>
            {
                baskets && products && (
                    <div>
                        <div className="row justify-content-center">
                            <h3>Create Ordered Product</h3>
                        </div>
                        <div className="row justify-content-center">
                            <Form onSubmit={handleSubmit}>
                                <Form.Group controlId={"createOrderedProduct"}>
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
                                    <Form.Label>Product</Form.Label>
                                    <Form.Control
                                        required
                                        name={"productId"}
                                        as={"select"}
                                        custom
                                    >
                                        {
                                            products.map(product => (
                                                <option key={product.id}
                                                        value={product.id}>{product.title}</option>
                                            ))
                                        }
                                    </Form.Control>
                                    <Form.Label>Quantity</Form.Label>
                                    <Form.Control
                                        required
                                        name={"quantity"}
                                        type="number"
                                        placeholder="Quantity"
                                    />
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

export default OrderedProductCreate;
