package ru.mail.polis;

import java.util.*;

public class AVLTree<E extends Comparable<E>> implements ISortedSet<E> {
    class AVLNode {
        AVLNode left;
        AVLNode right;
        AVLNode parent;
        E key;

        AVLNode(E k) {
            left = null;
            right = null;
            parent = null;
            key = k;
        }
    }

    private AVLNode root;
    private int size;
    private final Comparator<E> comparator;

    public AVLTree() {
        this.comparator = null;
    }

    public AVLTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public E first() {
        if (root == null) {
            throw new NoSuchElementException();
        } else {
            AVLNode min = root;
            while (min.left != null) {
                min = min.left;
            }
            return min.key;
        }
    }

    @Override
    public E last() {
        if (root == null) {
            throw new NoSuchElementException();
        } else {
            AVLNode max = root;
            while (max.right != null) {
                max = max.right;
            }
            return max.key;
        }
    }

    @Override
    public List<E> inorderTraverse() {
        List<E> list = new ArrayList<>();
        list = getList(root, list);
        return list;
    }

    private List<E> getList(AVLNode curr, List<E> list) {
        if (curr == null) return list;
        getList(curr.left, list);
        list.add(curr.key);
        getList(curr.right, list);
        return list;
    }

    @Override
    public int size() {
        int result = 0;
        if (root != null) {
            Queue<AVLNode> queue = new ArrayDeque<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                AVLNode curr = queue.poll();
                if (curr.left != null) {
                    queue.add(curr.left);
                }
                if (curr.right != null) {
                    queue.add(curr.right);
                }
                result++;
            }
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(E value) {
        if (value == null) {
            throw new NullPointerException();
        } else {
            return checkContains(root, value);
        }
    }

    private boolean checkContains(AVLNode node, E value) {
        if (node == null || node.key == null) {
            return false;
        } else if (compare(node.key, value) == 0) {
            return true;
        } else if (compare(node.key, value) == 1) {
            return checkContains(node.left, value);
        } else if (compare(node.key, value) == -1) {
            return checkContains(node.right, value);
        } else {
            return false;
        }
    }

    @Override
    public boolean add(E value) {
        if (value == null) {
            throw new NullPointerException();
        } else if (contains(value)) {
            return false;
        }
        AVLNode node = new AVLNode(value);

        insertAVL(this.root, node);
        return true;
    }

    private void insertAVL(AVLNode p, AVLNode q) {
        // Дерева еще нет, создаем корень.
        if (p == null) {
            this.root = q;
        } else {
            // Если ключ меньше, то идем к левому ребенку
            if (compare(q.key, p.key) == -1) {
                // Если его нет, то хорошо - вставляем
                if (p.left == null) {
                    p.left = q;
                    q.parent = p;
                    // Нужно сбалансировать
                    recursiveBalance(p);
                } else {
                    // Если есть левый ребенок, то продолжаем поиск
                    insertAVL(p.left, q);
                }
                // Если ключ больше, то для правого ребенка то же самое
            } else if (compare(q.key, p.key) == 1) {
                if (p.right == null) {
                    p.right = q;
                    q.parent = p;

                    recursiveBalance(p);
                } else {
                    insertAVL(p.right, q);
                }
            }
        }
    }


    /**
     * Проверяем баланс для нод рекурсивно. Балансируем, если нужно.
     *
     * @param cur Нода, которую проверяем на баланс.
     */
    private void recursiveBalance(AVLNode cur) {
        // Узнаем разницу в высотах.
        int balance = getBalance(cur);

        // Левая высота на 2 больше, надо балансировать.
        if (balance == -2) {
            if (height(cur.left.left) >= height(cur.left.right)) {
                cur = rotateRight(cur);
            } else {
                cur = bigRotateRight(cur);
            }
            // Правая высота больше, надо балансировать.
        } else if (balance == 2) {
            if (height(cur.right.right) >= height(cur.right.left)) {
                cur = rotateLeft(cur);
            } else {
                cur = bigRotateLeft(cur);
            }
        }

        // Есть же еще родитель, пока не достигли корня надо балансировать.
        if (cur.parent != null) {
            recursiveBalance(cur.parent);
        } else {
            // Закночили балонсировку.
            this.root = cur;
        }
    }

    // Вычисляет высоту для ноды.
    private int height(AVLNode cur) {
        // Если нет, то -1
        if (cur == null) {
            return -1;
        }
        if (cur.left == null && cur.right == null) {
            return 0;
        } else if (cur.left == null) {
            return 1 + height(cur.right);
        } else if (cur.right == null) {
            return 1 + height(cur.left);
        } else {
            return 1 + maximum(height(cur.left), height(cur.right));
        }
    }

    private int maximum(int a, int b) {
        return a >= b ? a : b;
    }

    private int getBalance(AVLNode cur) {
        return height(cur.right) - height(cur.left);
    }

    private AVLNode rotateLeft(AVLNode a) {
        // Просто делаем малый левый поворот как на вики итмо.
        AVLNode b = a.right;
        b.parent = a.parent;

        a.right = b.left;

        if (a.right != null) {
            a.right.parent = a;
        }

        b.left = a;
        a.parent = b;

        // Если есть узел выше, надо чтобы он указывал на поменянную ноду.
        if (b.parent != null) {
            if (b.parent.right == a) {
                b.parent.right = b;
            } else if (b.parent.left == a) {
                b.parent.left = b;
            }
        }

        return b;
    }

    private AVLNode rotateRight(AVLNode b) {
        // Тоже просто делаем малый правый поворот как на вики итмо.
        AVLNode a = b.left;
        a.parent = b.parent;

        b.left = a.right;

        if (b.left != null) {
            b.left.parent = b;
        }

        a.right = b;
        b.parent = a;

        // Если есть узел выше, надо чтобы он указывал на поменянную ноду.
        if (a.parent != null) {
            if (a.parent.right == b) {
                a.parent.right = a;
            } else if (a.parent.left == b) {
                a.parent.left = a;
            }
        }

        return a;
    }

    // Большой правый поворот
    private AVLNode bigRotateRight(AVLNode u) {
        u.left = rotateLeft(u.left);
        return rotateRight(u);
    }

    // Большой левый поворот
    private AVLNode bigRotateLeft(AVLNode u) {
        u.right = rotateRight(u.right);
        return rotateLeft(u);
    }

    @Override
    public boolean remove(E value) {
        if (value == null) {
            throw new NullPointerException();
        } else if (!contains(value)) {
            return false;
        }

        removeAVL(root, value);
        return true;
    }

    private void removeAVL(AVLNode p, E q) {
        if (compare(p.key, q) == 1) {
            // Ищем у левого ребенка.
            removeAVL(p.left, q);
        } else if (compare(p.key, q) == -1) {
            // Ну и у правого.
            removeAVL(p.right, q);
        } else if (compare(p.key, q) == 0) {
            // Нашли ноду, удаляем.
            removeFoundNode(p);
        }
    }

    /**
     * Удаляем ноду из дерева, балансировка будет при необходимости.
     *
     * @param q нода, которую удаляем.
     */
    private void removeFoundNode(AVLNode q) {
        AVLNode r;
        // Если нет хотя бы одного ребенка, то q сразу удалим.
        if (q.left == null || q.right == null) {
            // Значит, это корень.
            if (q.parent == null) {
                if (q.left != null) {
                    // Если это левый ребенок, то делаем его вершиной.
                    this.root = q.left;
                    q.left.parent = null;
                } else {
                    // Аналогично для правого.
                    this.root = q.right;
                    q.right.parent = null;
                }
                // Для сбощика мусора нуллим q.
                q = null;
                return;
            }
            r = q;
        } else {
            // У q есть два ребенка - с этим разберется successor.
            r = successor(q);
            q.key = r.key;
        }

        AVLNode p;
        if (r.left != null) {
            p = r.left;
        } else {
            p = r.right;
        }

        if (p != null) {
            p.parent = r.parent;
        }

        if (r.parent == null) {
            this.root = p;
        } else {
            if (r == r.parent.left) {
                r.parent.left = p;
            } else {
                r.parent.right = p;
            }
            // Балансировка должна быть закончена до того, как достигнем корня.
            recursiveBalance(r.parent);
        }
        // Опять для сборщика.
        r = null;
    }

    private AVLNode successor(AVLNode q) {//predecessor
        // Так почему-то реализовано в визуализаторе
        // Идем к левому ребенку, а потом по всем его правым, чтобы в будущем cвапнуть с удаляемым.
        AVLNode r = q.left;
        while (r.right != null) {
            r = r.right;
        }
        return r;
    }


    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }


    /**
     * Функции для печати дерева в человеческий вид
     */

    public void simmetr() {
        printLined(root);
    }

    private void printLined(AVLNode curr) {
        if (curr == null) return;
        printLined(curr.left);
        System.out.print(curr.key + " ");
        printLined(curr.right);
    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        printTree(root, "", sb);
        System.out.println(sb.toString());
    }

    private void printTree(AVLNode node, String indent, StringBuilder sb) {
        sb.append(node.key);
        sb.append("\n");
        if (node.left != null) {
            if (node.right == null) {
                appendChild(node.left, indent, sb);
                appendLastNullChild(indent, sb);
            } else {
                appendChild(node.left, indent, sb);
                appendLastChild(node.right, indent, sb);
            }
        } else if (node.right != null) {
            appendNullChild(indent, sb);
            appendLastChild(node.right, indent, sb);
        }
    }


    private void appendNullChild(String indent, StringBuilder sb) {
        sb.append(indent);
        sb.append('├');
        sb.append('─');
        sb.append("null\n");
    }

    private void appendLastNullChild(String indent, StringBuilder sb) {
        sb.append(indent);
        sb.append('└');
        sb.append('─');
        sb.append("null\n");
    }

    private void appendChild(AVLNode node, String indent, StringBuilder sb) {
        sb.append(indent);
        sb.append('├');
        sb.append('─');
        printTree(node, indent + "│ ", sb);
    }

    private void appendLastChild(AVLNode node, String indent, StringBuilder sb) {
        sb.append(indent);
        sb.append('└');
        sb.append('─');
        printTree(node, indent + "  ", sb);
    }

    /** Заканчиваются тут */
}
