import React, {useContext, useEffect, useState} from 'react';
import {Link} from "react-router-dom";
import {UserContext} from "../../../../providers/UserProvider";
import {deleteSubcategory, getSubcategories} from "../../../../services/SubcategoryService";
import SimplyNavigation from "../../SimplyNavigation";

function Subcategories() {
    const {user} = useContext(UserContext);
    const [subcategories, setSubcategories] = useState(null);

    useEffect(() => {
        getSubcategories()
            .then(subcategoriesFromRepo => setSubcategories(subcategoriesFromRepo))
    }, [setSubcategories]);

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel"} createLink={"/adminPanel/subcategory/create"}/>
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
