import React, {Component} from 'react';
import logo from '../logo-e-biznes.png'
import '../css/NavigationBar.css'

class NavigationBar extends Component {

    render() {
        return (
            <nav className="navbar navbar-light bg-light">
                <div className={"Logo"} onClick={this.props.hideBasket}>
                    <img
                        src={logo}
                        alt="logo"
                        className="d-inline-block align-top logo mr-4"
                    />
                </div>
                <form className="form-inline">
                    <button
                        className="btn btn-outline-primary my-2 my-sm-0"
                        type="button"
                        onClick={() => this.props.showBasket()}
                    >
                        Basket
                    </button>
                </form>
            </nav>
        );
    }
}

export default NavigationBar;
