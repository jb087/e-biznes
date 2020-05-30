import React from 'react';
import {Form} from "react-bootstrap";

function BasketIdSelector({baskets}) {
    return (
        <div>
            <Form.Label>BasketId</Form.Label>
            <Form.Control
                required
                name={"basketId"}
                as={"select"}
                custom
            >
                {
                    baskets.filter(basket => basket.isBought === 0)
                        .map(basket => (
                            <option key={basket.id}
                                    value={basket.id}>{basket.id}</option>
                        ))
                }
            </Form.Control>
        </div>
    );
}

export default BasketIdSelector;
