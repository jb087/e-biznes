import React, {Component} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import Categories from "./Categories";
import Products from "./Products";


class Home extends Component {

    state = {
        selectedSubcategoryId: ""
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
                            <Products selectedSubcategoryId={this.state.selectedSubcategoryId}/>
                        </Paper>
                    </Grid>
                </Grid>
            </div>
        );
    }

    selectSubcategory = (subcategoryId) => {
        this.setState({selectedSubcategoryId: subcategoryId});
    }
}

export default Home;
