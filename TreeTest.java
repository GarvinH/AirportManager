package Trees;
import java.util.Scanner;

public class TreeTest {
    public static void main(String[] args) {
        SortBTree<String> tree = new SortBTree<>();
        Scanner keyInput = new Scanner(System.in);
        System.out.println("Enter your name");
        String name = keyInput.next();
        tree.add(name);
        tree.add("jake");
        tree.add("a");
        tree.add("fal");
        tree.add("lals");
        tree.add("male");
        tree.add("talf");
        tree.add("ealet");
        tree.add("jbb");
        tree.add("jbc");
        tree.display();
        System.out.println(tree.size() + "size");
        tree.remove("jake");
        System.out.println(tree.contains("fal"));
        System.out.println(tree.size() + "size");
        tree.display();
    }

}
