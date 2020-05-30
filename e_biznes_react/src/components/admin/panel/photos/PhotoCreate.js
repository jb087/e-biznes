import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {getProducts} from "../../../../services/ProductService";
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {createPhoto} from "../../../../services/PhotoService";

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
                <Link to={"/adminPanel/photos"}>
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
