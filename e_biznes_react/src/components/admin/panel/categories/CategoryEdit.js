import React, {useContext, useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {editCategory, getCategoryById} from "../../../../services/CategoryService";
import {UserContext} from "../../../../providers/UserProvider";
import SimplyNavigation from "../../SimplyNavigation";

function CategoryEdit() {
    const {user} = useContext(UserContext);
    const {categoryId} = useParams();
    const [category, setCategory] = useState(null);

    useEffect(() => {
        getCategoryById(categoryId)
            .then(categoryFromRepo => setCategory(categoryFromRepo))
    }, [categoryId, setCategory]);

    function handleSubmit(event) {
        event.preventDefault();

        editCategory({
            id: categoryId,
            name: event.target.elements.name.value
        }, user)
    }

    return (
        <div>
            {
                category && (
                    <div>
                        <SimplyNavigation upperLink={"/adminPanel/categories"}/>
                        <div className="row justify-content-center">
                            <h3>Edit Category</h3>
                        </div>
                        <div className="row justify-content-center">
                            <Form onSubmit={handleSubmit}>
                                <Form.Group controlId={"editCategory"}>
                                    <Form.Label>Name</Form.Label>
                                    <Form.Control
                                        required
                                        name={"name"}
                                        type="text"
                                        defaultValue={category.name}
                                        placeholder="Category name"
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

export default CategoryEdit;
