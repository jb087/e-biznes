import React, {useContext, useEffect, useState} from 'react';
import {Modal} from "react-bootstrap";
import {UserContext} from "../providers/UserProvider";
import {getBoughtBasketsByUser} from "../services/BasketService";
import {getOrderedProducts} from "../services/OrderedProductsService";
import {getProducts} from "../services/ProductService";
import {getOrders} from "../services/OrderService";
import {getPayments} from "../services/PaymentService";
import {getPhotos, photoJPGById} from "../services/PhotoService";

function BoughtModal({show, onHide}) {
    const {user} = useContext(UserContext);
    const [baskets, setBaskets] = useState(null);
    const [orderedProducts, setOrderedProducts] = useState(null);
    const [products, setProducts] = useState(null);
    const [photos, setPhotos] = useState(null);
    const [orders, setOrders] = useState(null);
    const [payments, setPayments] = useState(null);

    useEffect(() => {
        if (user) {
            getBoughtBasketsByUser(user)
                .then(basketsFromRepo => setBaskets(basketsFromRepo));
            getOrderedProducts()
                .then(orderedProductsFromRepo => setOrderedProducts(orderedProductsFromRepo));
            getProducts()
                .then(productsFromRepo => setProducts(productsFromRepo));
            getPhotos()
                .then(photosFromRepo => setPhotos(photosFromRepo));
            getOrders()
                .then(ordersFromRepo => setOrders(ordersFromRepo));
            getPayments()
                .then(paymentsFromRepo => setPayments(paymentsFromRepo));
        }
    }, [user, setBaskets, setOrderedProducts, setProducts, setPhotos, setOrders, setPayments]);

    const getPaymentByBasket = (basket) => {
        const order = getOrderByBasket(basket);
        return payments.find(payment => payment.orderId === order.id);
    };

    const getOrderByBasket = (basket) => {
        return orders.find(order => order.basketId === basket.id)
    };

    const getOrderedProductsByBasket = (basket) => {
        return orderedProducts.filter(orderedProduct => orderedProduct.basketId === basket.id)
    };

    const getPhoto = (productId) => {
        return photos.find(photo => photo.productId === productId);
    };

    const getImage = (productId) => {
        return (
            <div>
                {
                    getPhoto(productId) ?
                        <img
                            src={photoJPGById + getPhoto(productId).id}
                            alt={"logo"}
                            height={"150"}
                            width={"150"}
                        /> :
                        <img alt={"logo"}/>
                }
            </div>
        )
    };

    return (
        <div>
            <Modal size={"lg"} show={show} onHide={() => onHide()}>
                <Modal.Header closeButton>
                    <h2>Bought</h2>
                </Modal.Header>
                <Modal.Body>
                    {
                        baskets && orderedProducts && products && photos && orders && payments && (
                            <div>
                                {
                                    baskets.map(basket => (
                                        <div key={basket.id}>
                                            <h3>{getPaymentByBasket(basket).date}</h3>
                                            {
                                                getOrderedProductsByBasket(basket).map(orderedProduct => (
                                                    <div key={orderedProduct.id}>
                                                        {
                                                            [products.find(product => product.id === orderedProduct.productId)]
                                                                .map(product => (
                                                                    <div key={product.id}
                                                                         className="d-flex justify-content-between">
                                                                        <div>
                                                                            {
                                                                                getImage(product.id)
                                                                            }
                                                                        </div>
                                                                        <div>
                                                                            <h4>{product.title}</h4>
                                                                            <h4>Quantity: {orderedProduct.quantity}</h4>
                                                                            <h4>Price: {product.price}</h4>
                                                                        </div>
                                                                    </div>
                                                                ))
                                                        }
                                                    </div>
                                                ))
                                            }
                                            <br/><br/>
                                        </div>
                                    ))
                                }
                            </div>
                        )
                    }
                </Modal.Body>
            </Modal>
        </div>
    );
}

export default BoughtModal;
