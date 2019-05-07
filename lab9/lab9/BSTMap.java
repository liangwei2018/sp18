package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Liang Wei
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (key == null) {
            throw new IllegalArgumentException("A null key for get()!");
        }
        if (p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            return getHelper(key, p.left);
        } else if (cmp > 0) {
            return getHelper(key, p.right);
        } else {
            return p.value;
        }

    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (key == null) {
            throw new IllegalArgumentException("A null key for put()!");
        }
        if (value == null) {
            throw new IllegalArgumentException("A null value for put()!");
        }
        if (p == null) {
            size += 1;
            return new Node(key, value);
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (cmp > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////


    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> hs = new HashSet<>();
        keySetHelper(hs, root);
        return hs;
    }

    private void keySetHelper(Set<K> hs, Node p) {
        if (p == null) {
            return;
        }
        hs.add(p.key);
        keySetHelper(hs, p.left);
        keySetHelper(hs, p.right);
    }

    /**
     * Finds the node with the smallest key.
     * @param p the node to be checked.
     * @return return the found smallest node.
     */
    private Node findMin(Node p) {
        if (p.left == null) {
            return p;
        } else {
            return findMin(p.left);
        }
    }

    /**
     * Remove the smallest key.
     * @param p the node to be checked.
     * @return the tree after the removal.
     *
     */
    private Node removeMin(Node p) {
        if (p.left == null) {
            return p.right;
        }
        p.left = removeMin(p.left);
        return p;
    }



    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */

    @Override
    public V remove(K key) {
        V ret = get(key);
        if (ret == null) {
            return null;
        }
        root = removeHelper(key, null, root);
        return ret;
    }

    private Node removeHelper(K key, V val, Node p) {
        if (key == null) {
            throw new IllegalArgumentException("A null key for remove()!");
        }
        if (p == null) {
            return null;
        }

        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = removeHelper(key, val, p.left);
        } else if (cmp > 0) {
            p.right = removeHelper(key, val, p.right);
        } else {
            if (val == null || val.equals(p.value)) {
                if (p.left == null) {
                    size -= 1;
                    return p.right;
                } else if (p.right == null) {
                    size -= 1;
                    return p.left;
                } else {
                    Node ret = p;
                    p = findMin(ret.right);
                    p.right = removeMin(ret.right);
                    p.left = ret.left;
                    size -= 1;
                }

            }
        }
        return p;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("A null key for remove()!");
        }
        if (value == null) {
            throw new IllegalArgumentException("A null value for remove()!");
        }
        V ret = get(key);
        if (ret == null || !ret.equals(value)) {
            return null;
        }
        root = removeHelper(key, value, root);
        return ret;
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTIterator(root);
    }

    private class BSTIterator implements Iterator<K> {
        private Stack<Node> stack;

        BSTIterator(Node p) {
            stack = new Stack<>();
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
        }

        /** @return whether the next smallest key in the BST exists. */
        @Override
        public boolean hasNext() {
            return (!stack.isEmpty());
        }

        /** @return the next smallest key in the BST. */
        @Override
        public K next() {
            Node current = stack.pop();
            K returnKey = current.key;
            if (current.right != null) {
                current = current.right;
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }

            }
            return returnKey;
        }

    }

}
