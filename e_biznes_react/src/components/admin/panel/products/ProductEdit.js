import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {getSubcategories} from "../../../../services/SubcategoryService";
import {editProduct, getProductById} from "../../../../services/ProductService";
import {useParams} from "react-router-dom";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import SimplyNavigation from "../../SimplyNavigation";

function ProductEdit() {
    const {user} = useContext(UserContext);
    const {productId} = useParams();
    const [subcategories, setSubcategories] = useState(null);
    const [product, setProduct] = useState(null);

    useEffect(() => {
        getSubcategories()
            .then(subcategoriesFromRepo => setSubcategories(subcategoriesFromRepo));
        getProductById(productId)
            .then(productFromRepo => setProduct(productFromRepo));
    }, [productId, setProduct, setSubcategories]);

    function handleSubmit(event) {
        event.preventDefault();

        editProduct({
            id: productId,
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
                product && subcategories && (
                    <div>
                        <div className="row justify-content-center">
                            <h3>Edit Product</h3>
                        </div>
                        <div className="row justify-content-center">
                            <Form onSubmit={handleSubmit}>
                                <Form.Group controlId={"editProduct"}>
                                    <Form.Label>Title</Form.Label>
                                    <Form.Control
                                        required
                                        name={"title"}
                                        type="text"
                                        placeholder="Title"
                                        defaultValue={product.title}
                                    />
                                    <Form.Label>Price</Form.Label>
                                    <Form.Control
                                        required
                                        name={"price"}
                                        type="number"
                                        step={"0.01"}
                                        placeholder="Price"
                                        defaultValue={product.price}
                                    />
                                    <Form.Label>Quantity</Form.Label>
                                    <Form.Control
                                        required
                                        name={"quantity"}
                                        type="number"
                                        placeholder="Quantity"
                                        defaultValue={product.quantity}
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
                                                product.subcategoryId === subcategory.id ?
                                                    <option key={subcategory.id}
                                                            value={subcategory.id}
                                                            selected={true}>{subcategory.name}</option> :
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
                                        defaultValue={product.description}
                                    /><br/>
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

export default ProductEdit;
