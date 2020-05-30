import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {getProducts} from "../../../../services/ProductService";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {createPhoto} from "../../../../services/PhotoService";
import SimplyNavigation from "../../SimplyNavigation";

function PhotoCreate() {
    const {user} = useContext(UserContext);
    const [products, setProducts] = useState(null);
    const [photo, setPhoto] = useState(null);

    useEffect(() => {
        getProducts()
            .then(productsFromRepo => setProducts(productsFromRepo))
    }, [setProducts]);

    function handleSubmit(event) {
        event.preventDefault();

        const photoForm = new FormData();
        photoForm.append('picture', photo);
        photoForm.append('productId', event.target.elements.productId.value);

        createPhoto(photoForm, user);
    }

    function onChangeHandler(event) {
        console.log(event.target.files[0]);
        setPhoto(event.target.files[0]);
    }

    return (
        <div>
            <SimplyNavigation upperLink={"/adminPanel/photos"}/>
            {
                products && (
                    <div>
                        <div className="row justify-content-center">
                            <h3>Create Photo</h3>
                        </div>
                        <div className="row justify-content-center">
                            <Form onSubmit={handleSubmit}>
                                <Form.Group controlId={"createPhoto"}>
                                    <Form.Label>Subcategory</Form.Label>
                                    <Form.Control
                                        required
                                        name={"productId"}
                                        as={"select"}
                                        custom
                                    >
                                        {
                                            products.map(product => (
                                                <option key={product.id}
                                                        value={product.id}>{product.title}</option>
                                            ))
                                        }
                                    </Form.Control>
                                    <Form.Label>Photo</Form.Label><br/>
                                    <input type="file" name="file" required={true} onChange={onChangeHandler}/>
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

export default PhotoCreate;
