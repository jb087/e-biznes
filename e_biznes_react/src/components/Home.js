import React, {Component} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import Categories from "./Categories";
import Products from "./Products";
import Product from "./Product";
import {getProductById} from "../services/ProductService";
import {getPhotosByProductId, photoJPGById} from '../services/PhotoService'
import {getOpinionsByProductId} from '../services/OpinionsService'

class Home extends Component {

    state = {
        selectedSubcategoryId: "",
        selectedProduct: null,
        productOpinions: null,
        productPhotos: null
    };

    useStyles = makeStyles((theme) => ({
        root: {
            flexGrow: 1,
        },
        paper: {
            padding: theme.spacing(2),
            textAlign: 'center',
            color: theme.palette.text.secondary,
        },
    }));

    render() {
        return (
            <div className={this.useStyles.root}>
                <Grid container spacing={3}>
                    <Grid item xs={3}>
                        <Paper className={this.useStyles.paper}>
                            <Categories onLabelClick={this.selectSubcategory}/>
                        </Paper>
                    </Grid>
                    <Grid item xs={9}>
                        <Paper className={this.useStyles.paper}>
                            {
                                this.state.selectedProduct === null ?
                                    <Products
                                        selectedSubcategoryId={this.state.selectedSubcategoryId}
                                        selectProduct={this.selectProduct}
                                    /> :
                                    <Product
                                        product={this.state.selectedProduct}
                                        opinions={this.state.productOpinions}
                                        photos={this.state.productPhotos}
                                    />
                            }
                        </Paper>
                    </Grid>
                </Grid>
            </div>
        );
    }

    selectSubcategory = (subcategoryId) => {
        this.setState({
            selectedSubcategoryId: subcategoryId,
            selectedProduct: null,
            productOpinions: null,
            productPhotos: null
        });
    };

    selectProduct = async (productId) => {
        const product = await getProductById(productId);
        const opinions = await getOpinionsByProductId(productId);
        const photos = await getPhotosByProductId(productId);
        const images = [];
        photos.forEach(photo => {
            images.push({
                original: photoJPGById + photo.id
            })
        });

        this.setState({
            selectedSubcategoryId: "",
            selectedProduct: product,
            productOpinions: opinions,
            productPhotos: images
        });
    }
}

export default Home;
