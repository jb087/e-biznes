import React, {Component} from 'react';
import {makeStyles} from "@material-ui/core/styles";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";

class Basket extends Component {

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
                    <Grid item xs={12}>
                        <Paper className={this.useStyles.paper}>
                            <h1>Basket</h1>
                        </Paper>
                    </Grid>
                </Grid>
            </div>
        );
    }
}

export default Basket;
