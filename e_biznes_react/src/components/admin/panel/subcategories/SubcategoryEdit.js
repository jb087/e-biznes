import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {Link, useParams} from "react-router-dom";
import {getCategories} from "../../../../services/CategoryService";
import {editSubcategory, getSubcategoryById} from "../../../../services/SubcategoryService";
import logo from "../../../../logo-e-biznes.png";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";

function SubcategoryEdit() {
    const {user} = useContext(UserContext);
    const {subcategoryId} = useParams();
    const [categories, setCategories] = useState(null);
    const [subcategory, setSubcategory] = useState(null);

    useEffect(() => {
        getCategories()
            .then(categoriesFromRepo => setCategories(categoriesFromRepo));
        getSubcategoryById(subcategoryId)
            .then(subcategoryFromRepo => setSubcategory(subcategoryFromRepo));
    }, [subcategoryId, setCategories, setSubcategory]);

    function handleSubmit(event) {
        event.preventDefault();

        editSubcategory({
            id: subcategoryId,
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
                categories && subcategory && (
                    <div>
                        <div className="row justify-content-center">
                            <h3>Edit Subcategory</h3>
                        </div>
                        <div className="row justify-content-center">
                            <Form onSubmit={handleSubmit}>
                                <Form.Group controlId={"editSubcategory"}>
                                    <Form.Label>Name</Form.Label>
                                    <Form.Control
                                        required
                                        name={"name"}
                                        type="text"
                                        defaultValue={subcategory.name}
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
                                                category.id === subcategory.parentId ?
                                                    <option key={category.id}
                                                            value={category.id}
                                                            selected={true}>{category.name}</option> :
                                                    <option key={category.id}
                                                            value={category.id}>{category.name}</option>
                                            ))
                                        }
                                    </Form.Control>
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

export default SubcategoryEdit;
