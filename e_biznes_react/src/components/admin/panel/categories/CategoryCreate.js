import React, {useContext} from 'react';
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {createCategory} from "../../../../services/CategoryService";
import {UserContext} from "../../../../providers/UserProvider";
import SimplyNavigation from "../../SimplyNavigation";

function CategoryCreate() {
    const {user} = useContext(UserContext);

    function handleSubmit(event) {
        event.preventDefault();

        createCategory({
            id: "",
            name: event.target.elements.name.value
        }, user)
    }

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel/categories"}/>
            <div className="row justify-content-center">
                <h3>Create Category</h3>
            </div>
            <div className="row justify-content-center">
                <Form onSubmit={handleSubmit}>
                    <Form.Group controlId={"createCategory"}>
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
