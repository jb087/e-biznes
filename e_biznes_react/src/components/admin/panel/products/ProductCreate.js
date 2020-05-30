import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {getSubcategories} from "../../../../services/SubcategoryService";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {createProduct} from "../../../../services/ProductService";
import SimplyNavigation from "../../SimplyNavigation";

function ProductCreate() {
    const {user} = useContext(UserContext);
    const [subcategories, setSubcategories] = useState(null);

    useEffect(() => {
        getSubcategories()
            .then(subcategoriesFromRepo => setSubcategories(subcategoriesFromRepo))
    }, [setSubcategories]);

    function handleSubmit(event) {
        event.preventDefault();

        createProduct({
            id: "",
            subcategoryId: event.target.elements.subcategoryId.value,
            title: event.target.elements.title.value,
            price: parseFloat(event.target.elements.price.value),
            quantity: parseInt(event.target.elements.quantity.value),
            date: "1970-01-01",
            description: event.target.elements.description.value
        }, user);
    }

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel/products"}/>
            {
                subcategories && (
                    <div>
                        <div className="row justify-content-center">
                            <h3>Create Product</h3>
                        </div>
                        <div className="row justify-content-center">
                            <Form onSubmit={handleSubmit}>
                                <Form.Group controlId={"createProduct"}>
                                    <Form.Label>Title</Form.Label>
                                    <Form.Control
                                        required
                                        name={"title"}
                                        type="text"
                                        placeholder="Title"
                                    />
                                    <Form.Label>Price</Form.Label>
                                    <Form.Control
                                        required
                                        name={"price"}
                                        type="number"
                                        step={"0.01"}
                                        placeholder="Price"
                                    />
                                    <Form.Label>Quantity</Form.Label>
                                    <Form.Control
                                        required
                                        name={"quantity"}
                                        type="number"
                                        placeholder="Quantity"
                                    />
                                    <Form.Label>Subcategory</Form.Label>
                                    <Form.Control
                                        required
                                        name={"subcategoryId"}
                                        as={"select"}
                                        custom
                                    >
                                        {
                                            subcategories.map(subcategory => (
                                                <option key={subcategory.id}
                                                        value={subcategory.id}>{subcategory.name}</option>
                                            ))
                                        }
                                    </Form.Control>
                                    <br/>
                                    <Form.Label>Description</Form.Label>
                                    <br/>
                                    <textarea
                                        name={"description"}
                                        placeholder={"Description"}
                                    /><br/>
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
                )
            }
        </div>
    );
}

export default ProductCreate;
