/*
 * [SortBTree.java]
 * Sorted Binary Tree, which is self-balanced as an AVL tree
 * Albert Quon
 * 2019/05/06
 */

public class SortBTree<E extends Comparable<E>> {
    private SortBTreeNode root;
    private Stack<E> itemStack; // for saving all items to be used for file saving

    /**
     * Finds if an object exists within the tree
     * @param item Item that is to be found
     * @return boolean value if item is found or not
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
     * Recursive helper to traverse the tree
     * @param node Current node
     * @param item Item for comparison
     * @return boolean value if item is found or not
     */
    private boolean containHelper(SortBTreeNode node, E item){
        if (node == null){
            return false;
        }
        if (node.getItem().equals(item)){ // item is found
            return true;
        }
        if (item.compareTo((E) node.getItem()) > 0) { // if item is compared to be lower then go to the left child
            return containHelper(node.getRight(), item);
        }
        if (item.compareTo((E) node.getItem()) < 0) { // if item is compared to be higher then go to the right child
            return containHelper(node.getLeft(), item);
        }
        return false; // not present
    }

    /**
     * Determines the size of the tree
     * @return integer value of size
     */
    public int size(){
        return sizeHelper(root);
    }

    /**
     * Recursive helper method to determine size of the tree
     * @param node
     * @return integer value of size
     */
    private int sizeHelper(SortBTreeNode node){
        if (node == null){
            return 0;
        }
        return (1 + sizeHelper(node.getLeft()) + sizeHelper(node.getRight()));
    }

    /**
     * Insertion method
     * @param item
     */
    public void add(E item){
        root = addHelper(root, item); // tree can change when adding
    }

    /**
     * Recursive helper for insertion
     * @param node Current node
     * @param item Item to be compared to other nodes with
     * @return A node that is balanced
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

        //****************************************BALANCE THE NODE************************************************

        // change height due to the addition of a node
        node.setHeight(node.getHeight()+1 + Integer.max(nodeHeight(node.getLeft()), nodeHeight(node.getRight())));
        int balance = balance(node); // find the balance based on the heights of its children

        // if node's balance is greater than 1 or less than -1, then it's unbalanced
        // check 4 cases of being unbalanced
        // rotate nodes to fix the balance while maintaining order

        // left left
        if ((balance > 1) && (item.compareTo((E)node.getLeft().getItem()) < 1)) {
            return rightRotate(node); // rotate right
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
     * Removes a node based on its item
     * @param item Item to be removed
     */
    public void remove(E item){
        root = removeHelper(root, item);
    }

    /**
     * Recursively loops through the tree to find the item, then balances the tree once found
     * @param node Current node
     * @param item Item to be removed
     * @return Balanced node
     */
    private SortBTreeNode removeHelper(SortBTreeNode node, E item){
        if (node == null) {
            return null;
        }
        // do regular remove
        if (item.compareTo((E) node.getItem()) > 0) {
            node.setRight(removeHelper(node.getRight(), item));
        } else if (item.compareTo((E) node.getItem()) < 0) {
            node.setLeft(removeHelper(node.getLeft(), item));
        } else { // item is found
            // handle cases
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

        //***************************************BALANCE THE NODE*********************************************
        if (node == null) { // if node is a leaf
            return null;
        }

        // reset height and find the difference in heights
        node.setHeight(Integer.max(nodeHeight(node.getRight()), nodeHeight(node.getLeft())) + 1); // new height based on children
        int balance = balance(node); // find the balance based on the heights of its children

        // if node's balance is greater than 1 or less than -1, then it's unbalanced
        // check 4 cases of being unbalanced
        // rotate nodes to fix the balance while maintaining order

        // left left
        if ((balance > 1) && (item.compareTo((E)node.getLeft().getItem()) < 1)) {
            return rightRotate(node); // rotate right
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
     * Finds the smallest node given a starting node
     * @param node Current node
     * @return Leftmost node from the starting node
     */
    private SortBTreeNode smallestNode(SortBTreeNode node){
        SortBTreeNode current = node;
        while (current.getLeft() != null) { // keeps going left
            current = current.getLeft();
        }
        return current;
    }

    /**
     * Determines if the tree is empty
     * @return boolean value that represents if tree is empty or not
     */
    public boolean isEmpty(){
        if (root == null) {
            return true;
        }
        return false;
    }

    /**
     * Outputs the tree's items in order
     */
    public void displayInOrder() {
        displayInOrderHelper(root);
    }

    /**
     * Recursively loops through the tree and outputs the tree's items in order
     */
    private void displayInOrderHelper(SortBTreeNode node) {
        if (node == null) {
            return;
        }
        displayInOrderHelper(node.getLeft()); //print leftmost node
        System.out.println(node.getItem()); // print current node
        displayInOrderHelper(node.getRight()); // print greater node

    }

    /**
     * Outputs the items from the nodes of the tree
     */
    public void display(){
        if (root != null) {
            displayHelper(root);
        } else {
            System.out.println("Empty!");
        }
    }

    /**
     * Recursively outputs the items of the nodes from the tree
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
     * Finds the height of the node and used in dealing with NullPointerException
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
     * Finds the balance factor of a node based on its children
     * @param node The balance factor of the node to be determined
     * @return integer value of the balance factor
     */
    private int balance(SortBTreeNode node) {
        if (node == null) { //deals with null pointer
            return 0;
        }
        return nodeHeight(node.getLeft()) - nodeHeight(node.getRight()); // height is based on subtree
    }

    /**
     * Rotates nodes in a counterclockwise direction while maintaining order
     * @param node The root node that is used to reference its successors
     * @return The new root node
     */
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

    /**
     * Rotates nodes in a clockwise direction while maintaining order
     * @param node The root node that is used to reference its successors
     * @return The new root node
     */
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
     * Saves all of the tree's items and put into a stack
     * @return Stack with sorted items
     */
    public Stack saveTree(){
        itemStack = new Stack<E>();
        traverse(root);
        return this.itemStack;
    }

    /**
     * Recursively goes through the tree and pushes the items into a stack in order
     * @param node Current node
     */
    private void traverse(SortBTreeNode node){
        if (node == null){
            return;
        } else {

            traverse(node.getRight()); // put in the greatest node first
            itemStack.push((E)node.getItem()); // put in current node
            traverse(node.getLeft()); // put in smallest node last


        }
    }
}
