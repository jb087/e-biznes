import React from 'react';
import {Form} from "react-bootstrap";

function ShippingInformationFormGroup() {
    return (
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
    );
}

export default ShippingInformationFormGroup;
