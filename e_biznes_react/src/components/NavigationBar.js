import React, {Component} from 'react';
import logo from '../logo-e-biznes.png'

class NavigationBar extends Component {

    render() {
        return (
            <nav className="navbar navbar-light bg-light">
                <img
                    src={logo}
                    alt="logo"
                    className="d-inline-block align-top logo mr-4"
                />
            </nav>
        );
    }
}

export default NavigationBar;
