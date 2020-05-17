import React from 'react';
import './App.css';
import Panel from "./components/Panel";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

function App() {

    return (
        <main className={"app"}>
            <Router>
                <Switch>
                    <Route path={"/"} component={Panel}/>
                </Switch>
            </Router>
        </main>
    );
}

export default App;
