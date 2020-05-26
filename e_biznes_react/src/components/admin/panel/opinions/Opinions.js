import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {deleteOpinionById, getOpinions} from "../../../../services/OpinionsService";
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";

function Opinions() {
    const {user} = useContext(UserContext);
    const [opinions, setOpinions] = useState(null);

    useEffect(() => {
        getOpinions()
            .then(opinionsFromRepo => setOpinions(opinionsFromRepo));
    }, [setOpinions]);

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
                opinions && (
                    opinions.map(opinion => (
                        <div key={opinion.id}>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Id: {opinion.id}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>ProductId: {opinion.productId}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Rating: {opinion.rating}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Opinion: {opinion.opinion}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <button
                                    className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                    onClick={() => deleteOpinionById(opinion.id, user)}
                                >
                                    Delete
                                </button>
                            </div>
                            <br/>
                        </div>
                    ))
                )
            }
        </div>
    );
}

export default Opinions;
