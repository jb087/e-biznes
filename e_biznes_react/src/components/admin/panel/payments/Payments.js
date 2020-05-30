import React, {useEffect, useState} from 'react';
import {deletePayment, finalizePayment, getPayments} from "../../../../services/PaymentService";
import SimplyNavigation from "../../SimplyNavigation";

function Payments() {
    const [payments, setPayments] = useState(null);

    useEffect(() => {
        getPayments()
            .then(paymentsFromRepo => setPayments(paymentsFromRepo));
    }, [setPayments]);

    return (
        <div>
            <SimplyNavigation backLink={"/adminPanel"} createLink={"/adminPanel/payment/create"}/>
            {
                payments && (
                    payments.map(payment => (
                        <div key={payment.id}>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>Id: {payment.id}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h3 className={"mr-2"}>OrderId: {payment.orderId}</h3>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Is done: {payment.isDone}</h4>
                            </div>
                            <div className="row justify-content-center">
                                <h4 className={"mr-2"}>Date: {payment.date}</h4>
                            </div>
                            {
                                payment.isDone === 0 && (
                                    <div className="row justify-content-center">
                                        <button
                                            className="btn btn-outline-primary my-2 my-sm-0 mr-2"
                                            onClick={() => finalizePayment(payment.id, () => alert("Problem with payment finalization!"))
                                                .then(response => alert("Payment finalized successfully!"))
                                                .then(response => window.location.reload(false))
                                            }
                                        >
                                            Pay
                                        </button>
                                        <button
                                            className="btn btn-outline-danger my-2 my-sm-0 mr-2"
                                            onClick={() => deletePayment(payment.id, () => alert("Problem with payment deletion!"))
                                                .then(response => alert("Payment deleted!"))
                                                .then(response => window.location.reload(false))
                                            }
                                        >
                                            Delete
                                        </button>
                                    </div>
                                )
                            }
                            <br/>
                        </div>
                    ))
                )
            }
        </div>
    );
}

export default Payments;
