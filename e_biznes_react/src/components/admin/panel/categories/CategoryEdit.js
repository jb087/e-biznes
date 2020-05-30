import React, {useContext, useEffect, useState} from 'react';
import {Link, useParams} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {editCategory, getCategoryById} from "../../../../services/CategoryService";
import {UserContext} from "../../../../providers/UserProvider";

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
            {
                category && (
                    <div>
                        {getNav()}
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
