

public class SortBTree<E extends Comparable<E>> {
    private SortBTreeNode root;
    private Stack<E> itemStack; // for saving all items to be used for file saving

    /**
     *
     * @param item
     * @return
     */
    public boolean contains(E item){
        if (root != null) {
            if(root.getItem().equals(item)){
                return true;
            }
        }
        return containHelper(root, item);
    }

    /**
     *
     * @param node
     * @param item
     * @return
     */
    private boolean containHelper(SortBTreeNode node, E item){
        if (node == null){
            return false;
        }
        if (node.getItem().equals(item)){
            return true;
        }
        if (item.compareTo((E) node.getItem()) > 0) {
            return containHelper(node.getRight(), item);
        }
        if (item.compareTo((E) node.getItem()) < 0) {
            return containHelper(node.getLeft(), item);
        }
        return false; // not unique
    }

    /**
     *
     * @return
     */
    public int size(){
        return sizeHelper(root);
    }

    /**
     *
     * @param node
     * @return
     */
    private int sizeHelper(SortBTreeNode node){
        if (node == null){
            return 0;
        }
        return (1 + sizeHelper(node.getLeft()) + sizeHelper(node.getRight()));
    }

    /**
     *
     * @param item
     */
    public void add(E item){
        root = addHelper(root, item);
    }

    /**
     *
     * @param node
     * @param item
     */
    private SortBTreeNode addHelper(SortBTreeNode node, E item){
        if (node == null){
            return (new SortBTreeNode(item));
        }
        if (item.compareTo((E) node.getItem()) > 0){
            node.setRight(addHelper(node.getRight(), item));
        } else if (item.compareTo((E) node.getItem()) < 0) {
            node.setLeft(addHelper(node.getLeft(), item));
        } else { // prevent duplicates
            return node;
        }
        // change height due to the addition of a node
        node.setHeight(node.getHeight()+1 + Integer.max(nodeHeight(node.getLeft()), nodeHeight(node.getRight())));
        int balance = balance(node);
        // if node's balance is greater than 1 or less than -1, then it's unbalanced
        // check 4 cases of being unbalanced

        // left left
        if ((balance > 1) && (item.compareTo((E)node.getLeft().getItem()) < 1)) {
            return rightRotate(node);
        } else if ((balance < -1) && (item.compareTo((E)node.getRight().getItem()) > 1)) { // right right
            return leftRotate(node);
        } else if ((balance > 1) && (item.compareTo((E)node.getLeft().getItem()) > 1)) { // left right
            node.setLeft(leftRotate(node.getLeft())); // new parent
            return rightRotate(node);
        } else if ((balance < -1) && (item.compareTo((E)node.getRight().getItem()) < 1)) { // right left
            node.setRight(rightRotate(node.getRight())); // new parent
            return leftRotate(node);
        }

        //unchanged node
        return node;
    }

    /**
     *
     * @param item
     * @return
     */
    public void remove(E item){
        root = removeHelper(root, item);
    }

    /**
     *
     * @param node
     * @param item
     * @return
     */
    private SortBTreeNode removeHelper(SortBTreeNode node, E item){ // if choosing left most, start right, then always go left and find smallest (if going right, start left, then always go right and find largest)
        if (node == null) {
            return null;
        }
        if (item.compareTo((E) node.getItem()) > 0) {
            node.setRight(removeHelper(node.getRight(), item));
        } else if (item.compareTo((E) node.getItem()) < 0) {
            node.setLeft(removeHelper(node.getLeft(), item));
        } else { // item is found
            // node with no child
            if (node.isLeaf()) {
                node = null;
            } else if ((node.getLeft() == null) || (node.getRight() == null)) { // node with one child
                if (node.getLeft() == null) {
                    node = node.getRight();
                } else {
                    node = node.getLeft();
                }
            } else {
                SortBTreeNode tempNode = smallestNode(node.getRight()); // find leftmost node on its right
                node.setItem(tempNode.getItem()); // replace current node with it
                removeHelper(node.getRight(), (E)tempNode.getItem()); // remove the leftmost node
            }
        }
        if (node == null) {
            return null;
        }

        node.setHeight(Integer.max(nodeHeight(node.getRight()), nodeHeight(node.getLeft())) + 1); // set the height of the current node
        int balance = balance(node);
        // if node's balance is greater than 1 or less than -1, then it's unbalanced
        // check 4 cases of being unbalanced

        // left left
        if ((balance > 1) && (item.compareTo((E)node.getLeft().getItem()) < 1)) {
            return rightRotate(node);
        } else if ((balance < -1) && (item.compareTo((E)node.getRight().getItem()) > 1)) { // right right
            return leftRotate(node);
        } else if ((balance > 1) && (item.compareTo((E)node.getLeft().getItem()) > 1)) { // left right
            node.setLeft(leftRotate(node.getLeft())); // new parent
            return rightRotate(node);
        } else if ((balance < -1) && (item.compareTo((E)node.getRight().getItem()) < 1)) { // right left
            node.setRight(rightRotate(node.getRight())); // new parent
            return leftRotate(node);
        }

        return node;

    }

    /**
     *
     * @param node
     * @return
     */
    private SortBTreeNode smallestNode(SortBTreeNode node){
        SortBTreeNode current = node;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    /**
     *
     * @return
     */
    public boolean isEmpty(){
        if (root == null) {
            return true;
        }
        return false;
    }

    /**
     *
     */
    public void displayInOrder() {
        displayInOrderHelper(root);
    }

    /**
     *
     */
    private void displayInOrderHelper(SortBTreeNode node) {
        if (node == null) {
            return;
        }
        displayInOrderHelper(node.getLeft());
        System.out.println(node.getItem());
        displayInOrderHelper(node.getRight());

    }

    /**
     *
     */
    public void display(){
        if (root != null) {
            displayHelper(root);
        } else {
            System.out.println("Empty!");
        }
    }

    /**
     *
     * @param node
     */
    private void displayHelper(SortBTreeNode node){
        if(node == null){
            return;
        }
        System.out.println("Parent: " + node.getItem());
        if(node.getRight() != null) {
            System.out.println("R.Child: " + node.getRight().getItem() + " ");
        }
        if(node.getLeft() != null) {
            System.out.println("L.Child: " + node.getLeft().getItem() + " ");
        }
        displayHelper(node.getRight());
        displayHelper(node.getLeft());

    }

    /**
     * Finds the height of the node and avoids dealing with NullPointerException
     * @param node the node
     * @return the height of the node
     */
    private int nodeHeight(SortBTreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.getHeight();
    }

    /**
     *
     * @param node
     * @return
     */
    private int balance(SortBTreeNode node) {
        if (node == null) {
            return 0;
        }
        return nodeHeight(node.getLeft()) - nodeHeight(node.getRight());
    }

    private SortBTreeNode leftRotate(SortBTreeNode node) {
        SortBTreeNode right = node.getRight();
        SortBTreeNode rightLeft = right.getLeft();

        // rotation (current node becomes the left child of its right node and takes its left child as its right)
        right.setLeft(node);
        node.setRight(rightLeft);

        // adjust heights based on their children and add 1 due to the rotation
        right.setHeight((Integer.max(nodeHeight(right.getLeft()), nodeHeight(right.getRight()))) + 1);
        node.setHeight((Integer.max(nodeHeight(node.getLeft()), nodeHeight(node.getRight())) + 1));

        return right; // return the node so it can be used for the parent
    }

    private SortBTreeNode rightRotate(SortBTreeNode node) {
        SortBTreeNode left = node.getLeft();
        SortBTreeNode leftRight = left.getRight();

        // rotation (current node becomes the right child of its left node and takes its right child as its left)
        left.setRight(node);
        node.setLeft(leftRight);

        // adjust heights based on their children and add 1 due to the rotation
        left.setHeight((Integer.max(nodeHeight(left.getLeft()), nodeHeight(left.getRight())) + 1));
        node.setHeight(Integer.max(nodeHeight(node.getLeft()), nodeHeight(node.getRight()) + 1));

        return left; // return the node so it can be used for the parent
    }

    /**
     *
     * @return
     */
    public Stack saveTree(){
        itemStack = new Stack<E>();
        traverse(root);
        return this.itemStack;
    }

    /**
     *
     * @param node
     */
    private void traverse(SortBTreeNode node){
        if (node == null){
            return;
        } else {
            traverse(node.getLeft());
            itemStack.push((E)node.getItem());
            traverse(node.getRight());


        }
    }
}
