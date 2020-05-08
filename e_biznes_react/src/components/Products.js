import React, {Component} from 'react';
import ProductCard from "./ProductCard";
import { getProducts } from '../services/ProductService'

class Products extends Component {

    state = {
        products: []
    };

    render() {
        return (
            <div>
                {
                    //TODO filter by selected subcategory
                    this.state.products
                        .map(product =>
                            <ProductCard key={product.id} product={product}/>
                        )
                }
            </div>
        );
    }

    async componentDidMount() {
        const products = await getProducts();
        this.setState({products: products});
    }
}

export default Products;
