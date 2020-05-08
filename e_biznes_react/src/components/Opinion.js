import React, {Component} from 'react';
import {makeStyles} from "@material-ui/core/styles";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Box from '@material-ui/core/Box';
import Rating from '@material-ui/lab/Rating';
import Typography from '@material-ui/core/Typography';

class Opinion extends Component {

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
                            <Box component="fieldset" mb={3} borderColor="transparent">
                                <Typography component="legend">Rating</Typography>
                                <Rating name="read-only" value={this.props.opinion.rating} readOnly/>
                            </Box>
                        </Paper>
                    </Grid>
                    <Grid item xs={9}>
                        <Paper className={this.useStyles.paper}>
                            <p>{this.props.opinion.opinion}</p>
                        </Paper>
                    </Grid>
                </Grid>
            </div>
        );
    }
}

export default Opinion;
