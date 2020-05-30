import React, {useContext, useEffect, useState} from 'react';
import {getBaskets} from "../../../../services/BasketService";
import {getProducts} from "../../../../services/ProductService";
import {editOrderedProduct, getOrderedProductById} from "../../../../services/OrderedProductsService";
import {useParams} from "react-router-dom";
import {UserContext} from "../../../../providers/UserProvider";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import SimplyNavigation from "../../SimplyNavigation";

function OrderedProductEdit() {
    const {user} = useContext(UserContext);
    const {orderedProductId} = useParams();
    const [orderedProduct, setOrderedProduct] = useState(null);
    const [baskets, setBaskets] = useState(null);
    const [products, setProducts] = useState(null);

    useEffect(() => {
        getOrderedProductById(orderedProductId)
            .then(orderedProductFromRepo => setOrderedProduct(orderedProductFromRepo));
        getBaskets()
            .then(basketsFromRepo => setBaskets(basketsFromRepo));
        getProducts()
            .then(productsFromRepo => setProducts(productsFromRepo))
    }, [orderedProductId, setOrderedProduct, setBaskets, setProducts]);

    function handleSubmit(event) {
        event.preventDefault();

        editOrderedProduct({
            id: orderedProductId,
            basketId: event.target.elements.basketId.value,
            productId: event.target.elements.productId.value,
            quantity: parseInt(event.target.elements.quantity.value),
        }, user);
    }

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel/orderedProducts"}/>
            {
                orderedProduct && baskets && products && (
                    <div>
                        <div className="row justify-content-center">
                            <h3>Edit Ordered Product</h3>
                        </div>
                        <div className="row justify-content-center">
                            <Form onSubmit={handleSubmit}>
                                <Form.Group controlId={"editOrderedProduct"}>
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
                                                    basket.id === orderedProduct.basketId ?
                                                        <option key={basket.id}
                                                                value={basket.id}
                                                                selected={true}>{basket.id}</option> :
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
                                                product.id === orderedProduct.productId ?
                                                    <option key={product.id}
                                                            value={product.id}
                                                            selected={true}>{product.title}</option> :
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
                                        defaultValue={orderedProduct.quantity}
                                    />
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

export default OrderedProductEdit;
