import React, {useContext, useEffect, useState} from 'react';
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";
import {deleteCategory, getCategories} from "../../../../services/CategoryService";
import {UserContext} from "../../../../providers/UserProvider";

function Categories() {
    const {user} = useContext(UserContext);
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
                        <div key={category.id}>
                            <div className="row justify-content-center">
                                <h3>Id: {category.id}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>{category.name}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <button
                                    className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                    onClick={() => deleteCategory(category.id, user)}
                                >
                                    Delete
                                </button>
                                <Link to={"/adminPanel/category/edit/" + category.id}>
                                    <button className="btn btn-outline-primary my-2 my-sm-0 mr-2">
                                        Edit
                                    </button>
                                </Link>
                            </div>
                            <br/>
                        </div>
                    ))
                )
            }
        </div>
    );
}

export default Categories;
