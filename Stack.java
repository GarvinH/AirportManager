

public class Stack<E> {
    StNode<E> top;

    public void push(E item){
        top = new StNode<>(item, top);
    }
    public E pop(){
        if (top == null){
            return null;
        }
        E item = top.getItem();
        top = top.getNext();
        return item;
    }

}

class StNode<E> {
    private E item;
    private StNode<E> next;

    public StNode(E item) {
        this.item=item;
        this.next=null;
    }

    public StNode(E item, StNode<E> next) {
        this.item=item;
        this.next=next;
    }

    public StNode<E> getNext(){
        return this.next;
    }

    public void setNext(StNode<E> next){
        this.next = next;
    }

    public E getItem(){
        return this.item;
    }
}
