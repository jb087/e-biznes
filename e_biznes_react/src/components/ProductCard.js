import React, {Component} from 'react';
import CardActionArea from "@material-ui/core/CardActionArea";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import CardActions from "@material-ui/core/CardActions";
import Button from "@material-ui/core/Button";
import Card from "@material-ui/core/Card";
import logo from "../logo-e-biznes.png";
import {makeStyles} from "@material-ui/core/styles";

class ProductCard extends Component {

    useStyles = makeStyles({
        root: {
            maxWidth: 345,
        }
    });

    render() {
        return (
            <div>
                <Card className={this.useStyles.root}>
                    <CardActionArea>
                        <img
                            src={logo} //TODO logo
                            alt="logo"
                            className="d-inline-block align-top logo mr-4"
                        />
                        <CardContent>
                            <Typography gutterBottom variant="h5" component="h2">
                                {this.props.product.title}
                            </Typography>
                        </CardContent>
                    </CardActionArea>
                    <CardActions>
                        <Button size="small" color="primary">
                            Share
                        </Button>
                        <Button size="small" color="primary">
                            Learn More
                        </Button>
                    </CardActions>
                </Card>
            </div>
        );
    }
}

export default ProductCard;
