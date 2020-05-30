import React, {Component} from 'react';
import ImageGallery from 'react-image-gallery';
import '../css/Product.css'
import Opinion from "./Opinion";
import Button from "react-bootstrap/Button";
import OpinionModal from "./OpinionModal";

class Product extends Component {

    state = {
        showOpinionModal: false
    };

    render() {
        return (
            <div className={"container"}>
                <OpinionModal
                    show={this.state.showOpinionModal}
                    onHide={this.hideOpinionModal}
                    productId={this.props.product.id}
                />
                <div className="row justify-content-center">
                    <h1>{this.props.product.title}</h1>
                </div>
                <br/><br/>
                <ImageGallery items={this.props.photos} showThumbnails={false}/>
                <br/><br/>
                <div className="d-flex justify-content-between">
                    <div>
                        <Button
                            variant="primary"
                            onClick={() => this.addProductToBasket()}
                        >
                            Add to Basket
                        </Button>
                    </div>
                    <div>
                        <h5>Quantity: {this.props.product.quantity}</h5>
                        <h5>Price: {this.props.product.price}</h5>
                        <h6>Create date: {this.props.product.date}</h6>
                    </div>
                </div>
                <br/><br/>
                <div className="d-flex justify-content-between">
                    <div>
                        <h3>Description:</h3>
                        <p>{this.props.product.description}</p>
                    </div>
                    <div>
                        <Button
                            variant="primary"
                            onClick={() => this.showOpinionModal()}
                        >
                            Add Opinion
                        </Button>
                    </div>
                </div>
                <br/><br/>
                <h3>Opinions:</h3>
                {
                    this.props.opinions
                        .map(opinion => <Opinion key={opinion.id} opinion={opinion}/>)
                }
            </div>
        );
    }

    addProductToBasket = () => {
        if (this.props.product.quantity > 0) {
            this.props.addOrderedProduct(this.props.product.id);

            alert("Pomyślnie dodano produkt do koszyka!")
        } else {
            alert("Niewystarczająca ilość produktu. Nie można dodać produktu do koszyka!")
        }
    };

    showOpinionModal = () => {
        this.setState({showOpinionModal: true});
    };

    hideOpinionModal = () => {
        this.setState({showOpinionModal: false});
    }
}

export default Product;
