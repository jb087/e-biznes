import React, {useContext, useEffect, useState} from 'react';
import {editShippingInformation, getShippingInformationById} from "../../../../services/ShippingInformationService";
import {useParams} from "react-router-dom";
import {UserContext} from "../../../../providers/UserProvider";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import SimplyNavigation from "../../SimplyNavigation";

function ShippingInformationEdit() {
    const {user} = useContext(UserContext);
    const {shippingInformationId} = useParams();
    const [shippingInformation, setShippingInformation] = useState(null);

    useEffect(() => {
        getShippingInformationById(shippingInformationId)
            .then(shippingInformationFromRepo => setShippingInformation(shippingInformationFromRepo));
    }, [shippingInformationId, setShippingInformation]);

    function handleSubmit(event) {
        event.preventDefault();

        editShippingInformation({
            id: shippingInformationId,
            firstName: event.target.elements.firstName.value,
            lastName: event.target.elements.lastName.value,
            email: event.target.elements.email.value,
            street: event.target.elements.street.value,
            houseNumber: event.target.elements.houseNumber.value,
            city: event.target.elements.city.value,
            zipCode: event.target.elements.zipCode.value
        }, user)
    }

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel/shippingInformation"}/>
            {
                shippingInformation && (
                    <div>
                        <div className="row justify-content-center">
                            <h3>Edit Shipping Information</h3>
                        </div>
                        <div className="row justify-content-center">
                            <Form onSubmit={handleSubmit}>
                                <Form.Group controlId={"editShippingInformation"}>
                                    <Form.Label>First name</Form.Label>
                                    <Form.Control
                                        required
                                        name={"firstName"}
                                        type="text"
                                        placeholder="First name"
                                        defaultValue={shippingInformation.firstName}
                                    />
                                    <Form.Label>Last name</Form.Label>
                                    <Form.Control
                                        required
                                        name={"lastName"}
                                        type="text"
                                        placeholder="Last name"
                                        defaultValue={shippingInformation.lastName}
                                    />
                                    <Form.Label>E-mail</Form.Label>
                                    <Form.Control
                                        required
                                        name={"email"}
                                        type="email"
                                        placeholder="e-mail"
                                        defaultValue={shippingInformation.email}
                                    />
                                    <Form.Label>Street</Form.Label>
                                    <Form.Control
                                        required
                                        name={"street"}
                                        type="text"
                                        placeholder="street"
                                        defaultValue={shippingInformation.street}
                                    />
                                    <Form.Label>House Number</Form.Label>
                                    <Form.Control
                                        required
                                        name={"houseNumber"}
                                        type="text"
                                        placeholder="house number"
                                        defaultValue={shippingInformation.houseNumber}
                                    />
                                    <Form.Label>City</Form.Label>
                                    <Form.Control
                                        required
                                        name={"city"}
                                        type="text"
                                        placeholder="city"
                                        defaultValue={shippingInformation.city}
                                    />
                                    <Form.Label>Zip-code</Form.Label>
                                    <Form.Control
                                        required
                                        name={"zipCode"}
                                        type="text"
                                        placeholder="zip-code"
                                        defaultValue={shippingInformation.zipCode}
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

export default ShippingInformationEdit;
