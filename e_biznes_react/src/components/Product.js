import React, {Component} from 'react';
import ImageGallery from 'react-image-gallery';
import '../css/Product.css'
import Opinion from "./Opinion";
import Button from "react-bootstrap/Button";

class Product extends Component {

    render() {
        return (
            <div className={"container"}>
                <div className="row justify-content-center">
                    <h1>{this.props.product.title}</h1>
                </div>
                <br/><br/>
                <ImageGallery items={this.props.photos} showThumbnails={false}/>
                <br/><br/>
                <Button
                    variant="primary"
                    onClick={() => this.addProductToBasket()}
                >
                    Add to Basket
                </Button>
                <br/><br/>
                <h3>Description:</h3>
                <p>{this.props.product.description}</p>
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
        this.props.addOrderedProduct(this.props.product.id);

        alert("Pomyślnie dodano produkt do koszyka!")
    }
}

export default Product;
