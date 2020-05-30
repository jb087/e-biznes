import React from 'react';
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {createShippingInformation} from "../../../../services/ShippingInformationService";
import SimplyNavigation from "../../SimplyNavigation";
import ShippingInformationFormGroup from "../../../ShippingInformationFormGroup";

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

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel/shippingInformation"}/>
            <div>
                <div className="row justify-content-center">
                    <h3>Create Shipping Information</h3>
                </div>
                <div className="row justify-content-center">
                    <Form onSubmit={handleSubmit}>
                        <ShippingInformationFormGroup/>
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
