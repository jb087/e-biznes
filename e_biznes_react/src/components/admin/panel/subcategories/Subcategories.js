import React, {useContext, useEffect, useState} from 'react';
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";
import {UserContext} from "../../../../providers/UserProvider";
import {deleteSubcategory, getSubcategories} from "../../../../services/SubcategoryService";

function Subcategories() {
    const {user} = useContext(UserContext);
    const [subcategories, setSubcategories] = useState(null);

    useEffect(() => {
        getSubcategories()
            .then(subcategoriesFromRepo => setSubcategories(subcategoriesFromRepo))
    }, [setSubcategories]);

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
                <Link to={"/adminPanel/subcategory/create"}>
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
                subcategories && (
                    subcategories.map(subcategory => (
                        <div key={subcategory.id}>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Id: {subcategory.id}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>CategoryId: {subcategory.parentId}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>{subcategory.name}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <button
                                    className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                    onClick={() => deleteSubcategory(subcategory.id, user)}
                                >
                                    Delete
                                </button>
                                <Link to={"/adminPanel/subcategory/edit/" + subcategory.id}>
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

export default Subcategories;
