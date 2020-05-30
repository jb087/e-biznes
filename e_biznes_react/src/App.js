import React from 'react';
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
import Products from "./components/admin/panel/products/Products";
import ProductCreate from "./components/admin/panel/products/ProductCreate";
import ProductEdit from "./components/admin/panel/products/ProductEdit";
import Opinions from "./components/admin/panel/opinions/Opinions";
import Photos from "./components/admin/panel/photos/Photos";
import PhotoCreate from "./components/admin/panel/photos/PhotoCreate";
import Baskets from "./components/admin/panel/basket/Baskets";
import OrderedProducts from "./components/admin/panel/orderedProducts/OrderedProducts";
import OrderedProductCreate from "./components/admin/panel/orderedProducts/OrderedProductCreate";
import OrderedProductEdit from "./components/admin/panel/orderedProducts/OrderedProductEdit";
import ShippingInformation from "./components/admin/panel/shippingInformation/ShippingInformation";
import ShippingInformationCreate from "./components/admin/panel/shippingInformation/ShippingInformationCreate";
import ShippingInformationEdit from "./components/admin/panel/shippingInformation/ShippingInformationEdit";
import Orders from "./components/admin/panel/orders/Orders";
import OrderCreate from "./components/admin/panel/orders/OrderCreate";
import OrderEdit from "./components/admin/panel/orders/OrderEdit";
import Payments from "./components/admin/panel/payments/Payments";
import PaymentCreate from "./components/admin/panel/payments/PaymentCreate";

function App() {

    return (
        <UserProvider>
            <main className={"app"}>
                <Router>
                    <Switch>
                        <Route path={"/adminPanel/payment/create"}>
                            <AuthAdminCheck>
                                <PaymentCreate/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/payments"}>
                            <AuthAdminCheck>
                                <Payments/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/order/edit/:orderId"}>
                            <AuthAdminCheck>
                                <OrderEdit/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/order/create"}>
                            <AuthAdminCheck>
                                <OrderCreate/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/orders"}>
                            <AuthAdminCheck>
                                <Orders/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/shippingInformation/edit/:shippingInformationId"}>
                            <AuthAdminCheck>
                                <ShippingInformationEdit/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/shippingInformation/create"}>
                            <AuthAdminCheck>
                                <ShippingInformationCreate/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/shippingInformation"}>
                            <AuthAdminCheck>
                                <ShippingInformation/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/orderedProduct/edit/:orderedProductId"}>
                            <AuthAdminCheck>
                                <OrderedProductEdit/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/orderedProduct/create"}>
                            <AuthAdminCheck>
                                <OrderedProductCreate/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/orderedProducts"}>
                            <AuthAdminCheck>
                                <OrderedProducts/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/baskets"}>
                            <AuthAdminCheck>
                                <Baskets/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/photo/create"}>
                            <AuthAdminCheck>
                                <PhotoCreate/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/photos"}>
                            <AuthAdminCheck>
                                <Photos/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/opinions"}>
                            <AuthAdminCheck>
                                <Opinions/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/product/edit/:productId"}>
                            <AuthAdminCheck>
                                <ProductEdit/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/product/create"}>
                            <AuthAdminCheck>
                                <ProductCreate/>
                            </AuthAdminCheck>
                        </Route>
                        <Route path={"/adminPanel/products"}>
                            <AuthAdminCheck>
                                <Products/>
                            </AuthAdminCheck>
                        </Route>
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
