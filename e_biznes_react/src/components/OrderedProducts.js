import React, {Component} from 'react';
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import OrderedProduct from "./OrderedProduct";
import {getProductById} from "../services/ProductService";
import {getPhotosByProductId} from "../services/PhotoService";
import PurchaseModal from "./PurchaseModal";

class OrderedProducts extends Component {

    state = {
        products: null,
        showPurchaseModal: false
    };

    render() {
        return (
            <div>
                <PurchaseModal
                    show={this.state.showPurchaseModal}
                    onHide={this.hidePurchaseModal}
                    orderedProducts={this.state.products}
                />
                {
                    this.state.products && (
                        <Form onSubmit={this.handleSubmit}>
                            {
                                this.state.products
                                    .filter(product => product.product.quantity > 0)
                                    .map(product =>
                                        <OrderedProduct
                                            key={product.product.id}
                                            product={product}
                                            setQuantity={this.setQuantity}
                                            deleteProduct={this.deleteProduct}
                                        />
                                    )
                            }
                            <br/><br/>
                            <div className="d-flex justify-content-between">
                                <div>
                                    <h4>Total price: {this.totalPrice()}</h4>
                                </div>
                                <div>
                                    <Button
                                        variant="primary"
                                        type="submit"
                                    >
                                        Buy
                                    </Button>
                                </div>
                            </div>
                            <br/>
                        </Form>
                    )
                }
            </div>
        );
    }

    async componentDidMount() {
        let products = [];

        for (let i = 0; i < this.props.orderedProducts.length; i++) {
            const product = await getProductById(this.props.orderedProducts[i].productId);
            const photos = await getPhotosByProductId(this.props.orderedProducts[i].productId);
            if (photos.length > 0) {
                products.push({
                    product: product,
                    quantity: 1,
                    photoId: photos[0].id
                })
            } else {
                products.push({
                    product: product,
                    quantity: 1,
                    photoId: null
                })
            }
        }

        products = products.sort(this.compareProducts);

        if (products.length === 0) {
            products = null
        }

        this.setState({products: products})
    }

    totalPrice = () => {
        return this.state.products
            .reduce((sum, product) => sum + (product.quantity * product.product.price), 0)
    };

    setQuantity = (event) => {
        event.preventDefault();

        let productToUpdate = this.state.products.filter(product => product.product.id === event.target.id.replace("group:", ""))[0];
        productToUpdate.quantity = event.target.value;

        let products = this.state.products.filter(product => product.product.id !== event.target.id.replace("group:", ""));
        products.push(productToUpdate);
        products = products.sort(this.compareProducts);

        this.setState({products: products});
    };

    compareProducts = (product1, product2) => {
        if (product1.product.title < product2.product.title) {
            return -1;
        }
        if (product1.product.title > product2.product.title) {
            return 1;
        }
        return 0;
    };

    handleSubmit = event => {
        event.preventDefault();

        this.showPurchaseModal();
    };

    showPurchaseModal = () => {
        this.setState({showPurchaseModal: true});
    };

    hidePurchaseModal = () => {
        this.setState({showPurchaseModal: false});
    };

    deleteProduct = (productId) => {
        const products = this.state.products
            .filter(product => product.product.id !== productId);

        if (products.length === 0) {
            this.setState({products: null});
        } else {
            this.setState({products: products});
        }
    };
}

export default OrderedProducts;
