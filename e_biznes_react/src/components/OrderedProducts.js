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
                />
                {
                    this.state.products && (
                        <Form onSubmit={this.handleSubmit}>
                            {
                                this.state.products
                                    .filter(product => product.product.quantity > 0)
                                    .map(product =>
                                        <OrderedProduct key={product.product.id} product={product}/>
                                    )
                            }
                            <br/><br/>
                            <div className="row justify-content-center">
                                <Button
                                    variant="primary"
                                    type="submit"
                                >
                                    Buy
                                </Button>
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
                    photoId: photos[0].id
                })
            } else {
                products.push({
                    product: product,
                    photoId: null
                })
            }
        }

        if (products.length === 0) {
            products = null
        }

        this.setState({products: products})
    }

    handleSubmit = event => {
        event.preventDefault();

        //this.props.addOrderedProduct(this.props.product.id, event.target.elements.addProductToBasket.value);

        this.showPurchaseModal();
    };

    showPurchaseModal = () => {
        this.setState({showPurchaseModal: true});
    };

    hidePurchaseModal = () => {
        this.setState({showPurchaseModal: false});
    }
}

export default OrderedProducts;
