import React, {useEffect, useState} from 'react';
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";
import {deleteCategory, getCategories} from "../../../../services/CategoryService";

function Categories() {

    const [categories, setCategories] = useState(null);

    useEffect(() => {
        getCategories()
            .then(categoriesFromRepo => setCategories(categoriesFromRepo))
    }, [setCategories]);

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
                <Link to={"/adminPanel/category/create"}>
                    <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                        Create
                    </button>
                </Link>
                <Link to={"/adminPanel"}>
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
                    categories.map(category => (
                        <div className="row justify-content-center" key={category.id}>
                            <h3 className={"mr-2"}>{category.name}</h3>
                            <button
                                className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                onClick={() => deleteCategory(category.id)}
                            >
                                Delete
                            </button>
                            <Link to={"/adminPanel/category/edit/" + category.id}>
                                <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                                    Edit
                                </button>
                            </Link>
                        </div>
                    ))
                )
            }
        </div>
    );
}

export default Categories;
