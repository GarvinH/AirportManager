public class PriorityQueueTemplate {
    public static void main(String[] args) {
        PriorityQueue<String> test = new PriorityQueue<String>();
        test.enqueue("test1", 1);
        test.enqueue("test1.5",2);
        test.enqueue("test2", 3);
        test.enqueue("test3", 5);
        test.enqueue("test4", 4);
        test.enqueue("test5", 2);
        test.enqueue("test6",5);
        test.enqueue("test7",2);
        System.out.println(test.isEmpty());
        for (int i = 0; i < test.getSize(); i++) {
            System.out.print(test.getItem(i) + " ");
        }
        System.out.println();
        System.out.println(test.dequeue());
        for (int i = 0; i < test.getSize(); i++) {
            System.out.print(test.getItem(i) + " ");
        }
        System.out.println();
        System.out.println(test.dequeue());
        for (int i = 0; i < test.getSize(); i++) {
            System.out.print(test.getItem(i) + " ");
        }
        System.out.println();
        System.out.println(test.getItem(1));
    }
}

class PriorityQueue<E> {
    private Node<E> head;
    private Node<E> tail;

    public boolean isEmpty() {
        if (head == null) {
            return true;
        } else {
            return false;
        }
    }

    public void enqueue(E item, int priority) {
        Node<E> tempNode = head;

        if (head == null) {
            head = new Node<E>(item, priority);
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
                tempNode.setNext(new Node(item, priority, null, tempNode));
                tail = tempNode.getNext();
            } else {
                tempNode = tempNode.getPrevious();
                tempNode.setNext(new Node(item, priority, tempNode.getNext(), tempNode));
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
        Node<E> tempNode = head;
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
        Node<E> tempNode = head;
        int index = 1;
        while(tempNode.getNext() != null) {
            tempNode = tempNode.getNext();
            index++;
        }
        return index;
    }

    public E getTail() {
        return tail.getItem();
    }
}

class Node<T> implements Comparable<Integer> {
    private T item;
    private int priority;
    private Node<T> next;
    private Node<T> previous;

    public Node(T item, int priority) {
        this.item = item;
        this.priority = priority;
        next = null;
        previous = null;
    }

    public Node(T item, int priority, Node<T> next, Node<T> previous) {
        this.item = item;
        this.priority = priority;
        this.next = next;
        this.previous = previous;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public int getPriority() {
        return priority;
    }

    public Node<T> getPrevious() {
        return this.previous;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public void setPrevious(Node<T> previous) {
        this.previous = previous;
    }

    public int compareTo(Integer otherPriority) {
        return this.priority-otherPriority;
    }

    public T getItem() {
        return item;
    }
}
