/*
 * PriorityQueue.java
 * Priority Queue data structure
 * Garvin Hui
 * 2019/05/12
 */

import java.lang.Long;//used for compareTo

class PriorityQueue<E> {
    private PQNode<E> head;

    /**
     * Checks if Priority Queue is empoty
     * @return returns whether queue is empty or not
     */
    public boolean isEmpty() {
        if (head == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds an item into Priority Queue
     * @param item item being added
     * @param priority long value where smaller number is higher priority
     */
    public void enqueue(E item, long priority) {
        PQNode<E> tempNode = head;

        if (head == null) {
            head = new PQNode<E>(item, priority);
        } else {
            while((tempNode.getNext() != null) && (tempNode.compareTo(priority) < 1)) {
                //System.out.println(tempNode.getItem() + " " + item);
                tempNode = tempNode.getNext();
            }

            if (tempNode.compareTo(priority) <= 0) {
                tempNode.setNext(new PQNode(item, priority, null, tempNode));
            } else {
                if (tempNode.getPrevious() != null) {
                    tempNode = tempNode.getPrevious();
                    tempNode.setNext(new PQNode(item, priority, tempNode.getNext(), tempNode));
                    tempNode.getNext().getNext().setPrevious(tempNode.getNext());
                } else {
                    tempNode.setPrevious(new PQNode(item, priority, tempNode, null));
                    head = tempNode.getPrevious();
                }
            }
        }
    }

    /**
     * Removes first item added
     * @return returns item to be removed
     */
    public E dequeue() {
        if (head == null) {
            return null;
        }
        E removed = head.getItem();
        head = head.getNext();
        return removed;
    }

    /**
     * gets item of a specific index
     * @param index where the item is in queue
     * @return returns item of specific index
     */
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

    /**
     * gets size of queue
     * @return size of Priority Queue
     */
    public int getSize() {
        if (head == null) {
            return 0;
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

/** -------------------- INNER CLASS FOR NODE ----------- **/
class PQNode<T> implements Comparable<Long> {
    private T item;
    private long priority;
    private PQNode<T> next;
    private PQNode<T> previous;

    /**
     * Constructor for making node (used for first node)
     * @param item item to be stored
     * @param priority priority of node
     */
    public PQNode(T item, long priority) {
        this.item = item;
        this.priority = priority;
        next = null;
        previous = null;
    }

    /**
     * Constructor for making remaining nodes
     * @param item item to be stored
     * @param priority priority of node
     * @param next node after this node
     * @param previous node before this node
     */
    public PQNode(T item, long priority, PQNode<T> next, PQNode<T> previous) {
        this.item = item;
        this.priority = priority;
        this.next = next;
        this.previous = previous;
    }

    /**
     * gets this node's next node
     * @return next node
     */
    public PQNode<T> getNext() {
        return this.next;
    }

    /**
     * gets priority of current node
     * @return priority of node
     */
    public long getPriority() {
        return priority;
    }

    /**
     * gets this node's previous node
     * @return previous node
     */
    public PQNode<T> getPrevious() {
        return this.previous;
    }

    /**
     * used for changing vallue of next node
     * @param next next node
     */
    public void setNext(PQNode<T> next) {
        this.next = next;
    }

    /**
     * used for changing vallue of next node
     * @param previous previous node
     */
    public void setPrevious(PQNode<T> previous) {
        this.previous = previous;
    }

    /**
     * Compares priority of current node to other node (Long allows for date and time to be compared as it takes at least 12 digits)
     * @param otherPriority priority of comparing node
     * @return returns int value of difference in priority
     */
    public int compareTo(Long otherPriority) {
        return (int)(this.priority-otherPriority);
    }

    /**
     * gets this node's item
     * @return item
     */
    public T getItem() {
        return item;
    }
}