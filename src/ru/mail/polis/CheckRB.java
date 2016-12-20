package ru.mail.polis;

import java.util.List;

/**
 * Created by olerom on 17.12.16.
 */
public class CheckRB {
    public static void main(String[] args) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        System.out.println(tree.isEmpty());

        tree.add(1);
        tree.simmetr();
        tree.add(2);
        tree.simmetr();
        tree.add(3);
        tree.simmetr();
        tree.add(4);
        tree.simmetr();
        tree.add(5);
        tree.simmetr();
        tree.add(6);
        tree.simmetr();
        tree.add(7);
        tree.simmetr();

        System.out.println(tree.first());
        System.out.println(tree.last());

        tree.add(982);
        tree.simmetr();
        tree.add(0);
        tree.simmetr();
        tree.add(990);
        tree.simmetr();
        tree.add(10);
        tree.simmetr();
        tree.add(-4);
        tree.simmetr();

        List<Integer> arrayList = tree.inorderTraverse();
        for (int x : arrayList) {
            System.out.printf(x + " ");
        }

        System.out.println();

        tree.remove(2);
        tree.remove(4);
        tree.remove(6);
        tree.simmetr();


        System.out.println(tree.isEmpty());
        System.out.println(tree.size());

        System.out.println("-------------------");
        System.out.println(tree.add(990));
        System.out.println(tree.remove(11111));

        tree.simmetr();

        System.out.println("....................");

        RedBlackTree<Integer> rbTree = new RedBlackTree<>();

        System.out.println(rbTree.remove(10));
        System.out.println(rbTree.add(10));

        System.out.println(rbTree.remove(10));
        System.out.println(rbTree.add(100));

        rbTree.simmetr();
    }
}
