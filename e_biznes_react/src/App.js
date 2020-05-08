import React from 'react';
import './App.css';
import Panel from "./components/Panel";
import Home from "./components/Home";

function App() {

    return (
        <main className={"app"}>
            <Panel>
                <Home/>
            </Panel>
        </main>
    );
}

export default App;
