import React, {Component} from 'react';
import '../css/ProductCard.css'
import ProductCard from "./ProductCard";
import {getProducts} from '../services/ProductService'
import {getPhotos} from '../services/PhotoService'

class Products extends Component {

    state = {
        products: [],
        photos: []
    };

    render() {
        return (
            <div className={"Products"}>
                {
                    this.state.products
                        .filter(product => product.subcategoryId === this.props.selectedSubcategoryId)
                        .map(product =>
                            <ProductCard
                                key={product.id}
                                product={product}
                                getPhoto={this.getFirstPhotoByProductId}
                                selectProduct={this.props.selectProduct}
                            />
                        )
                }
            </div>
        );
    }

    async componentDidMount() {
        const products = await getProducts();
        const photos = await getPhotos();
        this.setState({
            products: products,
            photos: photos
        });
    }

    getFirstPhotoByProductId = (productId) => {
        return this.state.photos
            .find(photo => photo.productId === productId)
    }
}

export default Products;
