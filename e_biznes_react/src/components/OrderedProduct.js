import React, {Component} from 'react';
import {Col, Row} from "react-bootstrap";
import Form from "react-bootstrap/Form";
import _ from "underscore";
import {photoJPGById} from "../services/PhotoService";

class OrderedProduct extends Component {

    render() {
        return (
            <Row>
                <Col>
                    <div className="row justify-content-center">
                        {
                            this.props.product.photoId == null ?
                                this.getNoImage() :
                                this.getImage()
                        }
                        <br/><br/>
                    </div>
                    <div className="row justify-content-center">
                        <h5>{this.props.product.product.title}</h5>
                    </div>
                </Col>
                <Col>
                    <Form.Group controlId="">
                        <Form.Label>Quantity</Form.Label>
                        <Form.Control as="select" custom>
                            {
                                _.range(1, this.props.product.product.quantity + 1)
                                    .map(option =>
                                        <option key={option}>{option}</option>
                                    )
                            }
                        </Form.Control>
                    </Form.Group>
                </Col>
            </Row>
        );
    }

    getImage() {
        return <img
            src={photoJPGById + this.props.product.photoId}
            alt={"logo"}
            height={"150"}
            width={"150"}
        />;
    }

    getNoImage() {
        return <img
            alt={"logo"}
        />;
    }
}

export default OrderedProduct;
