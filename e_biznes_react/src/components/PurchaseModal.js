import React, {Component} from 'react';
import {Form, Modal} from 'react-bootstrap'
import Button from "react-bootstrap/Button";

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
                                <Form.Group controlId={"finalizePurchase"}>
                                    <Form.Label>First name</Form.Label>
                                    <Form.Control
                                        required
                                        name={"firstName"}
                                        type="text"
                                        placeholder="First name"
                                    />
                                    <Form.Label>Last name</Form.Label>
                                    <Form.Control
                                        required
                                        name={"lastName"}
                                        type="text"
                                        placeholder="Last name"
                                    />
                                    <Form.Label>E-mail</Form.Label>
                                    <Form.Control
                                        required
                                        name={"email"}
                                        type="email"
                                        placeholder="e-mail"
                                    />
                                    <Form.Label>Street</Form.Label>
                                    <Form.Control
                                        required
                                        name={"street"}
                                        type="text"
                                        placeholder="street"
                                    />
                                    <Form.Label>House Number</Form.Label>
                                    <Form.Control
                                        required
                                        name={"houseNumber"}
                                        type="text"
                                        placeholder="house number"
                                    />
                                    <Form.Label>City</Form.Label>
                                    <Form.Control
                                        required
                                        name={"city"}
                                        type="text"
                                        placeholder="city"
                                    />
                                    <Form.Label>Zip-code</Form.Label>
                                    <Form.Control
                                        required
                                        name={"zipCode"}
                                        type="text"
                                        placeholder="zip-code"
                                    />
                                </Form.Group>
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

    finalizePurchase = (event) => {
        event.preventDefault();

        // TODO           console.log(event.target.elements.firstName.value);
    }
}

export default PurchaseModal;
