import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {deleteShippingInformationById, getShippingInformation} from "../../../../services/ShippingInformationService";
import {Link} from "react-router-dom";
import SimplyNavigation from "../../SimplyNavigation";

function ShippingInformation(props) {
    const {user} = useContext(UserContext);
    const [shippingInformation, setShippingInformation] = useState(null);

    useEffect(() => {
        getShippingInformation()
            .then(shippingInformationFromRepo => setShippingInformation(shippingInformationFromRepo));
    }, [setShippingInformation]);

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel"} createLink={"/adminPanel/shippingInformation/create"}/>
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
