import React, {Component} from 'react';
import CardActionArea from "@material-ui/core/CardActionArea";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import Card from "@material-ui/core/Card";
import {makeStyles} from "@material-ui/core/styles";
import {photoJPGById} from '../services/PhotoService'

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
                        {
                            this.props.getPhoto(this.props.product.id) ?
                            this.getImage() :
                                this.getNoImage()
                        }
                        <CardContent>
                            <Typography gutterBottom variant="h5" component="h2">
                                {this.props.product.title}
                            </Typography>
                        </CardContent>
                    </CardActionArea>
                </Card>
            </div>
        );
    }

    getImage() {
        return <img
            src={photoJPGById + this.props.getPhoto(this.props.product.id).id}
            alt={"logo"}
            height={"150"}
            width={"150"}
        />;
    }

    getNoImage() {
        return <img
            alt={"logo"}
        />;
    }
}

export default ProductCard;
