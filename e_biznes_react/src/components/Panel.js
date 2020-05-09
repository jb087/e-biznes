import React, {Component} from 'react';
import NavigationBar from "./NavigationBar";
import Basket from "./Basket";

class Panel extends Component {

    state = {
        showBasket: false
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
                        this.props.children
                }
            </div>
        );
    }

    showBasket = () => {
        this.setState({showBasket: true});
    };

    hideBasket = () => {
        this.setState({showBasket: false});
    }
}

export default Panel;
