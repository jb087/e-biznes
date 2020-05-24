import React from 'react';
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {createCategory} from "../../../../services/CategoryService";
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";

function CategoryCreate() {

    function handleSubmit(event) {
        event.preventDefault();

        createCategory({
            id: "",
            name: event.target.elements.name.value
        })
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
                <Link to={"/adminPanel/categories"}>
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
            <div className="row justify-content-center">
                <h3>Create Category</h3>
            </div>
            <div className="row justify-content-center">
                <Form onSubmit={handleSubmit}>
                    <Form.Group controlId={"finalizePurchase"}>
                        <Form.Label>Name</Form.Label>
                        <Form.Control
                            required
                            name={"name"}
                            type="text"
                            placeholder="Category name"
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
    );
}

export default CategoryCreate;
