import React from 'react';
import './App.css';
import Panel from "./components/Panel";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import UserProvider from "./providers/UserProvider";
import Auth from "./components/Auth";
import AdminPanel from "./components/admin/panel/AdminPanel";
import Categories from "./components/admin/panel/categories/Categories";
import CategoryCreate from "./components/admin/panel/categories/CategoryCreate";
import CategoryEdit from "./components/admin/panel/categories/CategoryEdit";
import AuthAdminCheck from "./components/admin/panel/AuthAdminCheck";
import Subcategories from "./components/admin/panel/subcategories/Subcategories";
import SubcategoryCreate from "./components/admin/panel/subcategories/SubcategoryCreate";
import SubcategoryEdit from "./components/admin/panel/subcategories/SubcategoryEdit";

function App() {

    return (
        <UserProvider>
            <main className={"app"}>
                <Router>
                    <Switch>
                        <Route path={"/adminPanel/subcategory/edit/:subcategoryId"}>
                            <AuthAdminCheck>
                                <SubcategoryEdit/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/subcategory/create"}>
                            <AuthAdminCheck>
                                <SubcategoryCreate/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/subcategories"}>
                            <AuthAdminCheck>
                                <Subcategories/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/category/edit/:categoryId"}>
                            <AuthAdminCheck>
                                <CategoryEdit/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/category/create"}>
                            <AuthAdminCheck>
                                <CategoryCreate/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/categories"}>
                            <AuthAdminCheck>
                                <Categories/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel"}>
                            <AuthAdminCheck>
                                <AdminPanel/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/auth/:provider"} component={Auth}/>
                        <Route path={"/"} component={Panel}/>
                    </Switch>
                </Router>
            </main>
        </UserProvider>
    );
}

export default App;
