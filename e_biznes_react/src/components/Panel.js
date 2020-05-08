import React, {Component} from 'react';
import NavigationBar from "./NavigationBar";

class Panel extends Component {

    render() {
        return (
            <div>
                <NavigationBar/>
                {this.props.children}
            </div>
        );
    }
}

export default Panel;
