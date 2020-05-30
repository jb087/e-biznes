import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {deleteOpinionById, getOpinions} from "../../../../services/OpinionsService";
import SimplyNavigation from "../../SimplyNavigation";

function Opinions() {
    const {user} = useContext(UserContext);
    const [opinions, setOpinions] = useState(null);

    useEffect(() => {
        getOpinions()
            .then(opinionsFromRepo => setOpinions(opinionsFromRepo));
    }, [setOpinions]);

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel"}/>
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
