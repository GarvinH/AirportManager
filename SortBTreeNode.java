

class SortBTreeNode<E> {
    private E item;
    private int height;
    private SortBTreeNode left, right;

    /**
     *
     * @param item
     * @param left
     * @param right
     */
    SortBTreeNode(E item, SortBTreeNode left, SortBTreeNode right){
        this.item = item;
        this.left = left;
        this.right = right;
        this.height = 1;
    }

    /**
     *
     * @param item
     */
    SortBTreeNode(E item){
        this.item = item;
        this.left = null;
        this.right = null;
    }

    /**
     *
     * @return
     */
    public E getItem() {
        return item;
    }

    /**
     *
     * @return
     */
    public SortBTreeNode getRight(){
        return this.right;
    }

    /**
     *
     * @return
     */
    public SortBTreeNode getLeft() {
        return left;
    }

    /**
     *
     * @param right
     */
    void setRight(SortBTreeNode right) {
        this.right = right;
    }

    /**
     *
     * @param left
     */
    public void setLeft(SortBTreeNode left) {
        this.left = left;
    }

    /**
     *
     * @param item
     */
    public void setItem(E item) {
        this.item = item;
    }

    /**
     *
     * @return
     */
    public boolean isLeaf(){
        if ((right == null) && (left == null)){
            return true;
        }
        return false;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
