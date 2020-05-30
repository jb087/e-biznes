import React from 'react';
import {Form, Modal} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {createOpinion} from "../services/OpinionsService";

function OpinionModal({show, onHide, productId}) {

    function handleSubmit(event) {
        event.preventDefault();

        createOpinion({
            id: "",
            productId: productId,
            opinion: event.target.elements.opinion.value,
            rating: parseInt(event.target.elements.rating.value)
        })
            .then(response => onHide())
    }

    return (
        <div>
            <Modal size={"lg"} show={show} onHide={() => onHide()}>
                <Modal.Header closeButton>
                    <h2>Add Opinion</h2>
                </Modal.Header>
                <Modal.Body>
                    <div className="row justify-content-center">
                        <Form onSubmit={handleSubmit}>
                            <Form.Group controlId={"addOpinion"}>
                                <Form.Label>Opinion</Form.Label><br/>
                                <textarea
                                    required
                                    name={"opinion"}
                                    placeholder={"Opinion"}
                                /><br/>
                                <Form.Label>Rating</Form.Label>
                                <Form.Control
                                    required
                                    name={"rating"}
                                    as={"select"}
                                    custom
                                >
                                    <option>1</option>
                                    <option>2</option>
                                    <option>3</option>
                                    <option>4</option>
                                    <option>5</option>
                                </Form.Control>
                            </Form.Group>
                            <Button
                                variant="primary"
                                type="submit"
                            >
                                Add
                            </Button>
                        </Form>
                    </div>
                </Modal.Body>
            </Modal>
        </div>
    );
}

export default OpinionModal;
