class SortBTreeNode<E> {
    private E item;
    private SortBTreeNode left, right;
    SortBTreeNode(E item, SortBTreeNode left, SortBTreeNode right){
        this.item = item;
        this.left = left;
        this.right = right;
    }

    SortBTreeNode(E item){
        this.item = item;
        this.left = null;
        this.right = null;
    }

    public E getItem() {
        return item;
    }

    public SortBTreeNode getRight(){
        return this.right;
    }

    public SortBTreeNode getLeft() {
        return left;
    }

    void setRight(SortBTreeNode right) {
        this.right = right;
    }

    public void setLeft(SortBTreeNode left) {
        this.left = left;
    }

    public void setItem(E item) {
        this.item = item;
    }

    boolean isLeaf(){
        if ((right == null) && (left == null)){
            return true;
        }
        return false;
    }
}
