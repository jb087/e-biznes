import React, {Component} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import Categories from "./Categories";


class Home extends Component {

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
                            <Categories/>
                        </Paper>
                    </Grid>
                    <Grid item xs={9}>
                        <Paper className={this.useStyles.paper}>xs=9</Paper>
                    </Grid>
                </Grid>
            </div>
        );
    }
}

export default Home;
