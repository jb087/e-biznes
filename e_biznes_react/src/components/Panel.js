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
                        <Basket/> :
                        <Home addOrderedProduct={this.addOrderedProduct} />
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

    addOrderedProduct = (productId, quantity) => {
        const orderedProducts = this.state.orderedProducts;
        orderedProducts.push({
            productId: productId,
            quantity: quantity
        });

        this.setState({orderedProducts: orderedProducts});
    }
}

export default Panel;
