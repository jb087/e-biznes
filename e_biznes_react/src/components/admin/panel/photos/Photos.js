import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {deletePhotoById, getPhotos, photoJPGById} from "../../../../services/PhotoService";
import {Link} from "react-router-dom";
import logo from "../../../../logo-e-biznes.png";

function Photos() {
    const {user} = useContext(UserContext);
    const [photos, setPhotos] = useState(null);

    useEffect(() => {
        getPhotos()
            .then(photosFromRepo => setPhotos(photosFromRepo));
    }, [setPhotos]);

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
                <Link to={"/adminPanel/photo/create"}>
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
                photos && (
                    photos.map(photo => (
                        <div key={photo.id}>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Id: {photo.id}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>ProductId: {photo.productId}</h3>
                            </div>
                            <div className="d-flex justify-content-between">
                                <div>
                                    <img
                                        src={photoJPGById + photo.id}
                                        alt={"logo"}
                                        height={"150"}
                                        width={"150"}
                                    />
                                </div>
                                <div>
                                    <button
                                        className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                        onClick={() => deletePhotoById(photo.id, user)}
                                    >
                                        Delete
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))
                )
            }
        </div>
    );
}

export default Photos;
