import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {getCategories} from "../../../../services/CategoryService";
import {createSubcategory} from "../../../../services/SubcategoryService";

function SubcategoryCreate() {
    const {user} = useContext(UserContext);
    const [categories, setCategories] = useState(null);

    useEffect(() => {
        getCategories()
            .then(categoriesFromRepo => setCategories(categoriesFromRepo))
    }, [setCategories]);

    function handleSubmit(event) {
        event.preventDefault();

        createSubcategory({
            id: "",
            parentId: event.target.elements.categoryId.value,
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
                <Link to={"/adminPanel/subcategories"}>
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
                categories && (
                    <div>
                        <div className="row justify-content-center">
                            <h3>Create Subcategory</h3>
                        </div>
                        <div className="row justify-content-center">
                            <Form onSubmit={handleSubmit}>
                                <Form.Group controlId={"createSubcategory"}>
                                    <Form.Label>Name</Form.Label>
                                    <Form.Control
                                        required
                                        name={"name"}
                                        type="text"
                                        placeholder="Subcategory name"
                                    />
                                    <Form.Label>Category</Form.Label>
                                    <Form.Control
                                        required
                                        name={"categoryId"}
                                        as={"select"}
                                        custom
                                    >
                                        {
                                            categories.map(category => (
                                                <option key={category.id} value={category.id}>{category.name}</option>
                                            ))
                                        }
                                    </Form.Control>
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

export default SubcategoryCreate;
