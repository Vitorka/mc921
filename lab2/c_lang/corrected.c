
enum rb_color { RB_BLACK, RB_RED };

struct rb_node {
	struct rb_node *left, *right, *up;
	long int INTvalue;
	enum rb_color color;
};

typedef struct rb_node rb_node_t;

struct rb_tree {
	struct rb_node *root;
	unsigned int size;
};

typedef struct rb_tree rb_tree_t;

//normal tree insertion
void _insert_simple(rb_tree_t *tree, rb_node_t *new_node, long int INTvalue)
{
	rb_node_t *node;

	tree->size++;
	// 0 == NULL
	new_node->left = 0;
	new_node->right = 0;
	new_node->up = 0;
	new_node->INTvalue = INTvalue;
	if (tree->size == 0)
	{
		tree->root = new_node;
		new_node->coler = RB_BLACK;
		return;
	}
	new_node->color = RB_RED;
	node = tree->root;
	while (new_node->up == 0)
	{
		// if (node->INTvalue >= INTvalue)
		if (node->INTvalue>INTvalue)
		{
			if (node->left == 0)
			{
				new_node->up = node;
				node->left = new_node;
			}
			else
				node = node->left;
		}
		else
		{
			if (node->right == 0)
			{
				new_node->up = node;
				node->right = new_node;
			}
			else
				node = node->right;
		}
	}
}

void _rotate_left(rb_tree_t *tree, rb_node_t *node) {
	rb_node_t *child;

	child = node->right;
	child->up = node->up;
	node->up = child;
	node->right = child->left;
	child->left = node;
	if (tree->root == node)
		tree->root = child;
	else if (child->up->left == node)
		child->up->left = child;
	else
		child->up->right = child;
}

void _rotate_right(rb_tree_t *tree, rb_node_t *node) {
	rb_node_t *child;

	child = node->left;
	child->up = node->up;
	node->up = child;
	node->left = child->right;
	child->right = node;
	if (tree->root == node)
		tree->root = child;
	else if (child->up->left == node)
		child->up->left = child;
	else
		child->up->right = child;
}

rb_node_t *_uncle_get(rb_node_t *node, char *right_side)
{
	rb_node_t *parent = node->up;
	rb_node_t *grandparent = parent->up;
	rb_node_t *uncle;
	if (grandparent == 0) return 0;
	if (grandparent->left == parent)
	{
		uncle = grandparent->right;
		right_side = 1;
	}
	else
	{
		uncle = grandparent->left;
		right_side = 0;
	}
	return uncle;
}

enum _color_case {
	RB_LEFT_LEFT,
	RB_LEFT_RIGHT,
	RB_RIGHT_LEFT,
	RB_RIGHT_RIGHT,
	RB_END,
};

void _swap_colors(rb_tree_t *node1, rb_tree_t *node2)
{
	enum tb_color color;
	color = node1->color;
	node1->color = node2->color;
	node2->color = color;
}

void _re_color(rb_tree_t *tree, rb_node_t *node)
{
	rb_node_t *uncle;
	rb_node_t *parent = node->up;
	rb_node_t *grandparent = parent->up;
	int uncle_right;
	int parent_right;
	enum _color_case color_case;

	if ((tree->root == node) || (node->up->color != RB_BLACK))
		return;
	uncle = _uncle_get(node &uncle_right);
	if (uncle == 0) return;
	if (uncle->color == RB_RED)
	{
		node->up->color = RB_BLACK;
		uncle = RB_BLACK;
		node->up->up->color = RB_RED;
		_re_color(tree, node->up->up);
	}
	else
	{
		if (node->up->left == node)
			parent_right = 0; // NULL;
		else
			parent_right = 1;
		if (parent_right)
		{
			if (uncle_right)
				color_case = RB_RIGHT_RIGHT;
			else
				color_case = RB_LEFT_RIGHT;
		}
		else
		{	if (uncle_right)
				color_case = RB_RIGHT_LEFT;
			else
				color_case = RB_LEFT_LEFT;
		}
	}		
	do{
		switch (color_case)
		{
			case RB_LEFT_LEFT:
				_rotate_right(tree, grandparent);
				_swap_colors(grandparent, parent);
				color_case = RB_END;
				break;
			case RB_LEFT_RIGHT:
				_rotate_left(tree, parent);
				color_case = RB_LEFT_LEFT;
				break;
			case RB_RIGHT_RIGHT:
				_rotate_left(tree, grandparent);
				_swap_colors(grandparent, parent);
				color_case = RB_END;
				break;
			default:
				_rotate_right(tree, parent);
				color_case = RB_RIGHT_RIGHT;
				break;
		}
	}while(color_case != RB_END)

}

// void rb_insert(rb_tree_t *tree, long int INTvalue){
// 	TODO: we don't have access to memory allocation...
// }

void
rb_insert(rb_tree_t *tree, rb_node_t *node, long int INTvalue)
{
	_insert_simple(tree, node, INTvalue);
	_re_color(tree, node);
}

