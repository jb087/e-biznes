import React, {Component} from 'react';
import NavigationBar from "./NavigationBar";
import Basket from "./Basket";
import Home from "./Home";

class Panel extends Component {

    state = {
        showBasket: false,
        orderedProducts: []
    };

    render() {
        return (
            <div>
                <NavigationBar
                    showBasket={this.showBasket}
                    hideBasket={this.hideBasket}
                />
                {
                    this.state.showBasket ?
                        <Basket orderedProducts={this.state.orderedProducts}/> :
                        <Home addOrderedProduct={this.addOrderedProduct}/>
                }
            </div>
        );
    }

    showBasket = () => {
        this.setState({showBasket: true});
    };

    hideBasket = () => {
        this.setState({showBasket: false});
    };

    addOrderedProduct = (productId) => {
        let orderedProducts = this.state.orderedProducts;
        if (orderedProducts.filter(orderedProduct => orderedProduct.productId === productId).length) {
            orderedProducts = orderedProducts.filter(orderedProduct => orderedProduct.productId !== productId)
        }

        orderedProducts.push({
            productId: productId,
        });

        this.setState({orderedProducts: orderedProducts});
    }
}

export default Panel;
