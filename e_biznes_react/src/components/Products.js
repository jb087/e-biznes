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
                    this.state.products
                        .filter(product => product.subcategoryId === this.props.selectedSubcategoryId)
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
