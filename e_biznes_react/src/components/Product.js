import React, {Component} from 'react';
import ImageGallery from 'react-image-gallery';
import '../css/Product.css'
import Opinion from "./Opinion";

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
                <p>{this.props.product.description}</p>
                <br/><br/>
                <h3>Opinions:</h3>
                {
                    this.props.opinions
                        .map(opinion => <Opinion opinion={opinion}/>)
                }
            </div>
        );
    }
}

export default Product;
