import React from 'react';
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {createShippingInformation} from "../../../../services/ShippingInformationService";

function ShippingInformationCreate() {
    function handleSubmit(event) {
        event.preventDefault();

        createShippingInformation({
            id: "",
            firstName: event.target.elements.firstName.value,
            lastName: event.target.elements.lastName.value,
            email: event.target.elements.email.value,
            street: event.target.elements.street.value,
            houseNumber: event.target.elements.houseNumber.value,
            city: event.target.elements.city.value,
            zipCode: event.target.elements.zipCode.value
        })
            .then(response => alert("Shipping Information created!"))
    }

    function getNav() {
        return <nav className="navbar navbar-light bg-light">
            <Link to={"/"}>
                <img
                    src={logo}
                    alt="logo"
                    className="d-inline-block align-top logo mr-4"
                />
            </Link>
            <form className="form-inline">
                <Link to={"/adminPanel/shippingInformation"}>
                    <button className="btn btn-outline-danger my-2 my-sm-0 mr-2">
                        Back
                    </button>
                </Link>
            </form>
        </nav>;
    }

    return (
        <div>
            {getNav()}
            <div>
                <div className="row justify-content-center">
                    <h3>Create Shipping Information</h3>
                </div>
                <div className="row justify-content-center">
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId={"createShippingInformation"}>
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
                        <Button
                            variant="primary"
                            type="submit"
                        >
                            Create
                        </Button>
                    </Form>
                </div>
            </div>
        </div>
    );
}

export default ShippingInformationCreate;
