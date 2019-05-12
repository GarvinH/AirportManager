/*
 * [SortBTreeNode.java]
 * Node for the binary tree
 * Albert Quon
 * 2019/05/06
 */

class SortBTreeNode<E> {
    private E item;
    private int height;
    private SortBTreeNode left, right;

    /**
     * Constructor for the node for the sorted binary tree
     * @param item Item associated with the node
     * @param left Left child of the node
     * @param right Right child of the node
     */
    SortBTreeNode(E item, SortBTreeNode left, SortBTreeNode right){
        this.item = item;
        this.left = left;
        this.right = right;
        this.height = 1; // node will have a height of one as itself
    }

    /**
     * Constructor for the node for the sorted binary tree
     * @param item Item associated with the node
     */
    SortBTreeNode(E item){
        this.item = item;
        this.left = null;
        this.right = null;
        this.height = 1;
    }

    /**
     * Gets the item associated with the node
     * @return Node item
     */
    public E getItem() {
        return item;
    }

    /**
     * Gets the right child of the node
     * @return the node of the right child
     */
    public SortBTreeNode getRight(){
        return this.right;
    }

    /**
     * Gets the left child of the node
     * @return the node of the left child
     */
    public SortBTreeNode getLeft() {
        return left;
    }

    /**
     * Sets the right child of the node to new node
     * @param right The new node
     */
    void setRight(SortBTreeNode right) {
        this.right = right;
    }

    /**
     * Sets the left child of the node to a new node
     * @param left The new node
     */
    public void setLeft(SortBTreeNode left) {
        this.left = left;
    }

    /**
     * Sets the item of the node to another item
     * @param item The new item
     */
    public void setItem(E item) {
        this.item = item;
    }

    /**
     * Checks if this node is a leaf
     * @return boolean value
     */
    public boolean isLeaf(){
        if ((right == null) && (left == null)){
            return true;
        }
        return false;
    }

    /**
     * Gets height of the node
     * @return integer value of height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height to a new integer value
     * @param height Integer value of height
     */
    public void setHeight(int height) {
        this.height = height;
    }

}

