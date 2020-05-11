import React, {Component} from 'react';
import {Modal} from 'react-bootstrap'

class PurchaseModal extends Component {
    render() {
        return (
            <Modal show={this.props.show} onHide={() => this.props.onHide()}>
                <Modal.Header closeButton>
                    <h3>Purchase Process</h3>
                </Modal.Header>
                <Modal.Body>
                    Woohoo, you're reading this text in a modal!
                </Modal.Body>
            </Modal>
        );
    }
}

export default PurchaseModal;
