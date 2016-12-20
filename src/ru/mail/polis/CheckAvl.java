package ru.mail.polis;

import java.util.List;

/**
 * Created by olerom on 13.12.16.
 */
public class CheckAvl {
    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();

        tree.add(1);
        tree.add(2);
        tree.add(3);
        tree.add(4);
        tree.add(5);
        tree.add(6);
        tree.add(7);
        tree.add(8);
        tree.add(9);
        tree.add(10);

        tree.print();

//        System.out.println(tree.remove(8));

        tree.remove(8);

        tree.add(100);
        tree.add(101);
        tree.add(102);
        tree.add(103);

        tree.print();

        System.out.println(tree.first());
        System.out.println(tree.last());
        System.out.println(tree.size());
        List<Integer> list = tree.inorderTraverse();
        for (Integer x : list) {
            System.out.print(x + " ");
        }
        System.out.println();
        tree.simmetr();

        System.out.println();
        System.out.println("....................");

        AVLTree<Integer> avlTree = new AVLTree<>();

        System.out.println(avlTree.remove(10));
        System.out.println(avlTree.add(10));

        System.out.println(avlTree.remove(10));
        System.out.println(avlTree.add(100));

        avlTree.simmetr();
    }
}
