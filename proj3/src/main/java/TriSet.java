import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * A Trie class, used for prefix matching, autocomplete functions.
 *
 */
public class TriSet {

    //private static int R = 26;
    private Node root;

    public static class Node implements Comparable<Node> {
        private char ch;
        private int value;
        private int best;
        private boolean isKey;
        //private Node parent;
        private Map<Character, Node> next = new HashMap<>();
        /*
        Node() {
            c = '/';
            value = null;
            best = null;
            isKey = false;
        }

        Node(char c, int value, boolean isKey, Node prev) {
            this.c = c;
            this.value = value;
            this.best = value;
            this.isKey = isKey;
            this.parent = prev;
        }*/

        boolean isKey() {
            return isKey;
        }
        /*
        V getValue() {
            return value;
        }

        V getBest() {
            return best;
        }

        Map<Character, Node> getNext() {
            return next;
        }

        Set<Character> getNextKeySet() {
            return next.keySet();
        }*/

        @Override
        public int compareTo(Node that) {
            return Integer.compare(-1 * this.best, -1 * that.best);
        }
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or 0 if this Trie contains no mapping for the key.
     * @param key the key string
     * @return the value corresponding to the key node.
     */
    public int get(String key) {
        Node node = getHelp(root, key, 0);
        if (node == null) {
            return 0;
        }
        return  node.value;
    }

    /**
     * Searches for the node corresponding to the last
     * character of the string s, no matter s is a key or not.
     *
     * @param sNode the node to visit
     * @param s the string to be searched
     * @param l the position in the string s
     * @return the node corresponding to the last char of s.
     */
    private Node getHelp(Node sNode, String s, int l) {
        if (sNode == null) {
            return null;
        }
        if (l == s.length()) {
            return sNode;
        }
        Character c = s.charAt(l);
        return getHelp(sNode.next.get(c), s, l + 1);
    }

    /**
     * Associates the specified values with the specified key in this Trie.
     * @param key the specified key
     * @param val the specified values
     *
     */
    public void put(String key, int val) {
        root = putHelp(root, key, val, 0);
    }

    private Node putHelp(Node sNode, String key, int val, int l) {
        if (sNode == null) {
            sNode = new Node();
        }

        if (sNode.best < val) {
            sNode.best = val;
        }

        if (l > 0 && l < key.length()) {
            sNode.ch = key.charAt(l - 1);
        }

        if (l == key.length()) {
            sNode.ch = key.charAt(l - 1);
            sNode.value = val;
            sNode.isKey = true;
            return sNode;
        }
        char c = key.charAt(l);


        //sNode.next.put(c, putHelp(sNode.next.get(c), key, val, l + 1, sNode));
        sNode.next.put(c, putHelp(sNode.next.get(c), key, val, l + 1));
        return sNode;

    }

    /**
     *
     * @return A list of all keys in the Trie.
     */
    public List<String> getKeys() {
        List<String> keysList = new ArrayList<>();
        for (Character c : root.next.keySet()) {
            getKeysHelp(c.toString(), keysList, root.next.get(c));
        }
        return keysList;
    }

    private void getKeysHelp(String s, List<String> keysList, Node o) {
        if (o.isKey) {
            keysList.add(s);
        }
        for (Character c : o.next.keySet()) {
            getKeysHelp(s + c, keysList, o.next.get(c));
        }
    }

    /**
     *
     * @param s the prefix string
     * @return A list of strings starting with s
     */
    List<String> keysWithPrefix(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        Node sNode = getHelp(root, s, s.length());
        if (sNode == null) {
            return null;
        }
                /*root.next.get(s.charAt(0));
        for (int i = 1; i < s.length(); i += 1) {
            sNode = sNode.next.get(s.charAt(i));
        }*/

        List<String> keysList = new ArrayList<>();
        StringBuilder t = new StringBuilder(s);
        Queue<Node> pq = new PriorityQueue<>();
        //StringBuilder temp = t;

        pq.addAll(sNode.next.values());
        int num = 0;
        while (!pq.isEmpty()) {
            Node topNode = pq.poll();
            t.append(topNode.ch);

            if (topNode.isKey()) {
                /*while (topNode.parent.parent != null) {

                    topNode = topNode.parent;
                }*/
                keysList.add(t.toString());

                if (topNode.next.isEmpty()) {
                    t = new StringBuilder(s);
                }

                num += 1;
                if (num > 3) {
                    break;
                }
            }
            pq.addAll(topNode.next.values());
        }

        return keysList;
    }

}
