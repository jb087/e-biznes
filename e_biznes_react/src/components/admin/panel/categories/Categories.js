import React, {useContext, useEffect, useState} from 'react';
import {Link} from "react-router-dom";
import {deleteCategory, getCategories} from "../../../../services/CategoryService";
import {UserContext} from "../../../../providers/UserProvider";
import SimplyNavigation from "../../SimplyNavigation";

function Categories() {
    const {user} = useContext(UserContext);
    const [categories, setCategories] = useState(null);

    useEffect(() => {
        getCategories()
            .then(categoriesFromRepo => setCategories(categoriesFromRepo))
    }, [setCategories]);

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel"} createLink={"/adminPanel/category/create"}/>
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
