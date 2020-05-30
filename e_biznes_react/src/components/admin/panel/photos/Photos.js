import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../../../../providers/UserProvider";
import {deletePhotoById, getPhotos, photoJPGById} from "../../../../services/PhotoService";
import SimplyNavigation from "../../SimplyNavigation";

function Photos() {
    const {user} = useContext(UserContext);
    const [photos, setPhotos] = useState(null);

    useEffect(() => {
        getPhotos()
            .then(photosFromRepo => setPhotos(photosFromRepo));
    }, [setPhotos]);

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel"} createLink={"/adminPanel/photo/create"}/>
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
