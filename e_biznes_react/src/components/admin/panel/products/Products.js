import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {deleteProductById, getProducts} from "../../../../services/ProductService";
import {Link} from "react-router-dom";
import SimplyNavigation from "../../SimplyNavigation";

function Products() {
    const {user} = useContext(UserContext);
    const [products, setProducts] = useState(null);

    useEffect(() => {
        getProducts()
            .then(productsFromRepo => setProducts(productsFromRepo))
    }, [setProducts]);

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel"} createLink={"/adminPanel/product/create"}/>
            {
                products && (
                    products.map(product => (
                        <div key={product.id}>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Id: {product.id}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>SubcategoryId: {product.subcategoryId}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Title: {product.title}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Price: {product.price}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Quantity: {product.quantity}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Date: {product.date}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Description: {product.description}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <button
                                    className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                    onClick={() => deleteProductById(product.id, user)}
                                >
                                    Delete
                                </button>
                                <Link to={"/adminPanel/product/edit/" + product.id}>
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

export default Products;
