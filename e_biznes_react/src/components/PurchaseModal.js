import React, {Component} from 'react';
import {Modal} from 'react-bootstrap'
import PurchaseStepWizard from "./PurchaseStepWizard";

class PurchaseModal extends Component {
    render() {
        return (
            <Modal show={this.props.show} onHide={() => this.props.onHide()}>
                <Modal.Header closeButton>
                    <h3>Purchase Process</h3>
                </Modal.Header>
                <Modal.Body>
                    <PurchaseStepWizard/>
                </Modal.Body>
            </Modal>
        );
    }
}

export default PurchaseModal;
