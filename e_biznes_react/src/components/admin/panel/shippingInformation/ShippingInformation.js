import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {deleteShippingInformationById, getShippingInformation} from "../../../../services/ShippingInformationService";
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";

function ShippingInformation(props) {
    const {user} = useContext(UserContext);
    const [shippingInformation, setShippingInformation] = useState(null);

    useEffect(() => {
        getShippingInformation()
            .then(shippingInformationFromRepo => setShippingInformation(shippingInformationFromRepo));
    }, [setShippingInformation]);

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
                <Link to={"/adminPanel/shippingInformation/create"}>
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
                shippingInformation && (
                    shippingInformation.map(information => (
                        <div key={information.id}>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Id: {information.id}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>First Name: {information.firstName}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Last Name: {information.lastName}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>E-mail: {information.email}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Street: {information.street} {information.houseNumber}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>City: {information.city}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Zip Code: {information.zipCode}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <button
                                    className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                    onClick={() => deleteShippingInformationById(information.id, user)}
                                >
                                    Delete
                                </button>
                                <Link to={"/adminPanel/shippingInformation/edit/" + information.id}>
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

export default ShippingInformation;
