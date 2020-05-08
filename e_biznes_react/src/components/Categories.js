import React, {Component} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import TreeView from '@material-ui/lab/TreeView';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import TreeItem from '@material-ui/lab/TreeItem';
import {getCategories} from '../services/CategoryService'
import {getSubcategories} from '../services/SubcategoryService'

class Categories extends Component {

    state = {
        categories: [],
        subcategories: []
    };

    useStyles = makeStyles({
        root: {
            height: 240,
            flexGrow: 1,
            maxWidth: 400,
        },
    });

    render() {
        return (
            <div>
                <TreeView
                    className={this.useStyles.root}
                    defaultCollapseIcon={<ExpandMoreIcon/>}
                    defaultExpandIcon={<ChevronRightIcon/>}
                >
                    {
                        this.state.categories
                            .map(category =>
                                <TreeItem key={category.id} nodeId={category.id} label={category.name}>
                                    {
                                        this.state.subcategories
                                            .filter(subcategory => subcategory.parentId === category.id)
                                            .map(subcategory =>
                                                <TreeItem key={subcategory.id} nodeId={subcategory.id} label={subcategory.name}/>
                                            )
                                    }
                                </TreeItem>
                            )
                    }
                </TreeView>
            </div>
        );
    }

    async componentDidMount() {
        const categories = await getCategories();
        const subcategories = await getSubcategories();
        this.setState({
            categories: categories,
            subcategories: subcategories
        })
    }
}

export default Categories;
