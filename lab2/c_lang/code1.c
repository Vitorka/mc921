
ENUM rb_color { RB_BLACK, RB_RED };

STRUCT rb_node {
	STRUCT rb_node *left, *right, *up;
	LONG INT INTvalue;
	ENUM rb_color color;
	INT INTTEST_rr;
	INT INTTEST_rl;
};

TYPEDEF STRUCT rb_node rb_node_t;

STRUCT rb_tree {
	STRUCT rb_node *root;
	unsigned INT size;
};

TYPEDEF STRUCT rb_tree rb_tree_t;

//normal tree insertion
VOID _insert_simple(rb_tree_t *tree, rb_node_t *new_node, LONG INT INTvalue)
{
	rb_node_t *node;

	tree->size++;
	// 0 == NULL
	new_node->left = 0;
	new_node->right = 0;
	new_node->up = 0;
	new_node->INTvalue = INTvalue;
	new_node->INTTEST_rr = 0;
	new_node->INTTEST_rl = 0;
	IF (tree->size == 0)
	{
		tree->root = new_node;
		new_node->coler = RB_BLACK;
		RETURN;
	}
	new_node->color = RB_RED;
	node = tree->root;
	WHILE (new_node->up == 0)
	{
		// IF (node->INTvalue >= INTvalue)
		IF (node->INTvalue>INTvalue)
		{
			IF (node->left == 0)
			{
				new_node->up = node;
				node->left = new_node;
			}
			ELSE
				node = node->left;
		}
		ELSE
		{
			IF (node->right == 0)
			{
				new_node->up = node;
				node->right = new_node;
			}
			ELSE
				node = node->right;
		}
	}
}

VOID _rotate_left(rb_tree_t *tree, rb_node_t *node) {
	rb_node_t *child;

	node->INTTEST_rl++;
	child = node->right;
	child->up = node->up;
	node->up = child;
	node->right = child->left;
	child->left = node;
	IF (tree->root == node)
		tree->root = child;
	ELSE IF (child->up->left == node)
		child->up->left = child;
	ELSE
		child->up->right = child;
}

VOID _rotate_right(rb_tree_t *tree, rb_node_t *node) {
	rb_node_t *child;

	node->INTTEST_rr++;
	child = node->left;
	child->up = node->up;
	node->up = child;
	node->left = child->right;
	child->right = node;
	IF (tree->root == node)
		tree->root = child;
	ELSE IF (child->up->left == node)
		child->up->left = child;
	ELSE
		child->up->right = child;
}

rb_node_t *_uncle_get(rb_node_t *node, CHAR *right_side)
{
	rb_node_t *parent = node->up;
	rb_node_t *grandparent = parent->up;
	rb_node_t *uncle;
	IF (grandparent == 0) RETURN 0;
	IF (grandparent->left == parent)
	{
		uncle = grandparent->right;
		right_side = 1;
	}
	ELSE
	{
		uncle = grandparent->left;
		right_side = 0;
	}
	RETURN uncle;
}

ENUM _color_CASE {
	RB_LEFT_LEFT,
	RB_LEFT_RIGHT,
	RB_RIGHT_LEFT,
	RB_RIGHT_RIGHT,
	RB_END,
};

VOID _swap_colors(rb_tree_t *node1, rb_tree_t *node2)
{
	ENUM tb_color color;
	color = node1->color;
	node1->color = node2->color;
	node2->color = color;
}

VOID _re_color(rb_tree_t *tree, rb_node_t *node)
{
	rb_node_t *uncle;
	rb_node_t *parent = node->up;
	rb_node_t *grandparent = parent->up;
	INT uncle_right;
	INT parent_right;
	ENUM _color_CASE color_case;

	IF ((tree->root == node) || (node->up->color != RB_BLACK))
		RETURN;
	uncle = _uncle_get(node &uncle_right);
	IF (uncle == 0) RETURN;
	IF (uncle->color == RB_RED)
	{
		node->up->color = RB_BLACK;
		uncle = RB_BLACK;
		node->up->up->color = RB_RED;
		_re_color(tree, node->up->up);
	}
	ELSE
	{
		IF (node->up->left == node)
			parent_right = 0; // NULL;
		ELSE
			parent_right = 1;
		IF (parent_right)
		{
			IF (uncle_right)
				color_CASE = RB_RIGHT_RIGHT;
			ELSE
				color_CASE = RB_LEFT_RIGHT;
		}
		ELSE
		{	IF (uncle_right)
				color_CASE = RB_RIGHT_LEFT;
			ELSE
				color_CASE = RB_LEFT_LEFT;
		}
	}		
	do{
		SWITCH (color_CASE)
		{
			CASE RB_LEFT_LEFT:
				_rotate_right(tree, grandparent);
				_swap_colors(grandparent, parent);
				color_CASE = RB_END;
				BREAK;
			CASE RB_LEFT_RIGHT:
				_rotate_left(tree, parent);
				color_CASE = RB_LEFT_LEFT;
				BREAK;
			CASE RB_RIGHT_RIGHT:
				_rotate_left(tree, grandparent);
				_swap_colors(grandparent, parent);
				color_CASE = RB_END;
				BREAK;
			default:
				_rotate_right(tree, parent);
				color_CASE = RB_RIGHT_RIGHT;
				BREAK;
		}
	}WHILE(color_CASE != RB_END)

}

// VOID rb_insert(rb_tree_t *tree, LONG INT INTvalue){
// 	TODO: we don't have access to memory allocation...
// }

VOID
rb_insert(rb_tree_t *tree, rb_node_t *node, LONG INT INTvalue)
{
	_insert_simple(tree, node, INTvalue);
	_re_color(tree, node);
}

INT TEST_rr(rb_tree_t *tree){RETURN tree->INTTEST_rr;}
INT	TEST_rl(rb_tree_t *tree){RETURN tree->INTTEST_rl;}
