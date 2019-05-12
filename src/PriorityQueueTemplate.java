
public class PriorityQueue<E> {
    private PQNode<E> head;
    private PQNode<E> tail;

    public boolean isEmpty() {
        if (head == null) {
            return true;
        } else {
            return false;
        }
    }

    public void enqueue(E item, int priority) {
        PQNode<E> tempNode = head;

        if (head == null) {
            head = new PQNode<E>(item, priority);
            tail = head;
        } else {
            while((tempNode.getNext() != null) && (tempNode.compareTo(priority) < 1)) {
                System.out.println(tempNode.getItem() + " " + item);
                tempNode = tempNode.getNext();
            }

            /*if (tempNode.compareTo(priority) == 1) {
                if (item.equals("test7") || item.equals("test5")) {
                    System.out.println(item);
                    System.out.println("here");
                    System.out.println(tempNode.getItem());
                    System.out.println(tempNode.getPrevious().getItem());
                }
                tempNode = tempNode.getPrevious();
                tempNode.setNext(new Node(item, priority, tempNode.getNext(), tempNode));
            } else*/ if (tempNode.compareTo(priority) <= 0) {
                tempNode.setNext(new PQNode(item, priority, null, tempNode));
                tail = tempNode.getNext();
            } else {
                tempNode = tempNode.getPrevious();
                tempNode.setNext(new PQNode(item, priority, tempNode.getNext(), tempNode));
                tempNode.getNext().getNext().setPrevious(tempNode.getNext());
            }
        }
    }

    public E dequeue() {
        E removed = head.getItem();
        head = head.getNext();
        return removed;
    }

    public E getItem(int index) {
        PQNode<E> tempNode = head;
        int tempIndex = 0;
        if (head == null) {
            return null;
        } else {
            while((tempNode.getNext()) != null && (tempIndex<index)) {
                tempNode = tempNode.getNext();
                tempIndex++;
            }
            if (tempIndex == index) {
                return tempNode.getItem();
            } else {
                return null;
            }
        }
    }

    public int getSize() {
        if (head == null) {
            return -1;
        }
        PQNode<E> tempNode = head;
        int index = 1;
        while(tempNode.getNext() != null) {
            tempNode = tempNode.getNext();
            index++;
        }
        return index;
    }
}

class PQNode<T> implements Comparable<Integer> {
    private T item;
    private int priority;
    private PQNode<T> next;
    private PQNode<T> previous;

    public PQNode(T item, int priority) {
        this.item = item;
        this.priority = priority;
        next = null;
        previous = null;
    }

    public PQNode(T item, int priority, Node<T> next, Node<T> previous) {
        this.item = item;
        this.priority = priority;
        this.next = next;
        this.previous = previous;
    }

    public PQNode<T> getNext() {
        return this.next;
    }

    public int getPriority() {
        return priority;
    }

    public PQNode<T> getPrevious() {
        return this.previous;
    }

    public void setNext(PQNode<T> next) {
        this.next = next;
    }

    public void setPrevious(PQNode<T> previous) {
        this.previous = previous;
    }

    public int compareTo(Integer otherPriority) {
        return this.priority-otherPriority;
    }

    public T getItem() {
        return item;
    }
}
