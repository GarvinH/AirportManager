
/*
 * [Stack.java]
 * Stack data structure
 * Albert Quon
 * 2019/05/06
 */

public class Stack<E> {
    StNode<E> top;

    /**
     * Adds a new item to the stack
     * @param item Item to be pushed
     */
    public void push(E item){
        top = new StNode<>(item, top);
    }

    /**
     * Returns the item on the top of the stack
     * @return Topmost item
     */
    public E pop(){
        if (top == null){
            return null;
        }
        E item = top.getItem();
        top = top.getNext();
        return item;
    }

}
/** --------- INNER CLASS FOR NODE ------------- **/

/*
 * [StNode.java]
 * Nodes for the stack
 * Albert Quon
 * 2019/05/06
 */
class StNode<E> {
    private E item;
    private StNode<E> next;

    /**
     * Constructor for the node
     * @param item
     */
    public StNode(E item) {
        this.item=item;
        this.next=null;
    }

    /**
     * Constructor for a node
     * @param item Item stored in the node
     * @param next The next node
     */
    public StNode(E item, StNode<E> next) {
        this.item=item;
        this.next=next;
    }

    /**
     * Returns the next node
     * @return The next node
     */
    public StNode<E> getNext(){
        return this.next;
    }

    /**
     * Sets the next node
     * @param next New node to be set as the next node
     */
    public void setNext(StNode<E> next){
        this.next = next;
    }

    /**
     * Get the item associated with the node
     * @return item
     */
    public E getItem(){
        return this.item;
    }
}
