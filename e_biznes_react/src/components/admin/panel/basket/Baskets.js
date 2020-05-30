import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {createBasket, deleteBasketById, editBasket, getBaskets} from "../../../../services/BasketService";
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";
import {check400Status} from "../../../../utils/RequestUtils";

function Baskets() {
    const {user} = useContext(UserContext);
    const [baskets, setBaskets] = useState(null);

    useEffect(() => {
        getBaskets()
            .then(basketsFromRepo => setBaskets(basketsFromRepo));
    }, [setBaskets]);

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
                <button
                    className="btn btn-outline-primary my-2 my-sm-0 mr-2"
                    onClick={() => createBasket({
                        id: "",
                        isBought: 0
                    })
                        .then(response => check400Status(response))
                        .then(response => alert("Basket created!"))
                        .then(response => window.location.reload(false))
                    }
                >
                    Create
                </button>
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
                baskets && (
                    baskets.map(basket => (
                        <div key={basket.id}>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Id: {basket.id}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Is bought: {basket.isBought}</h4>
                            </div>
                            <div className="row justify-content-center">
                                {
                                    basket.isBought === 0 && (
                                        <div>
                                            <button
                                                className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                                onClick={() => deleteBasketById(basket.id, user)}
                                            >
                                                Delete
                                            </button>
                                            <button
                                                className="btn btn-outline-primary my-2 my-sm-0 mr-2"
                                                onClick={() => editBasket({
                                                    id: basket.id,
                                                    isBought: 1
                                                }, user)}
                                            >
                                                Make bought
                                            </button>
                                        </div>
                                    )
                                }
                            </div>
                            <br/>
                        </div>
                    ))
                )
            }
        </div>
    );
}

export default Baskets;
