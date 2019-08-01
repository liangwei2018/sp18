import java.util.ArrayDeque;
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
    private static final int LENGTH = 5; // the first LENGTH of the string key used for comparison
                                        // spaces will be counted for shorter strings.

    public static class Node implements Comparable<Node> {
        private char ch;
        private int value;
        private int best;
        private boolean isKey;
        private Node parent;
        private Map<Character, Node> next = new HashMap<>();


        boolean isKey() {
            return isKey;
        }

        @Override
        public int compareTo(Node that) {
            return Integer.compare(this.best, that.best);
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
     * Add the specified key to the Trie. compute its value (val) and best,
     * based on ASCII of the first LENGTH letters in the key string.
     * @param key the specified key
     *
     */
    public void put(String key) {
        if (key == null || key.isEmpty()) {
            return;
        }
        int val = 0;
        int size = key.length();
        for (int i = 0; i < Math.min(size, LENGTH); i += 1) {
            int space = 32;
            int m = key.charAt(i) - space;
            val = val * 31 + m;
        }
        if (size < LENGTH) {
            for (int i = size; i < LENGTH; i += 1) {
                val = val * 31;
            }
        }
        root = putHelp(root, key,  val, 0, null);
    }

    private Node putHelp(Node sNode, String key, int val, int l, Node prev) {
        if (sNode == null) {
            sNode = new Node();
            sNode.parent = prev;
        }

        if (sNode.best < val) {
            sNode.best = val;
        }

        if (l > 0 && l < key.length()) {
            sNode.ch = key.charAt(l - 1);
        }

        if (l == key.length() && l > 0) {
            sNode.ch = key.charAt(l - 1);
            sNode.value = val;
            sNode.best = val;
            sNode.isKey = true;
            return sNode;
        }
        char c = key.charAt(l);

        sNode.next.put(c, putHelp(sNode.next.get(c), key, val, l + 1, sNode));
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
        if (s == null || root == null) {
            return null;
        }

        Node sNode = getHelp(root, s, 0);
        if (sNode == null) {
            return null;
        }

        List<String> keysList = new ArrayList<>();
        Queue<Node> pq = new PriorityQueue<>();
        Queue<Node> topNodeQueue = new ArrayDeque<>();
        pq.add(sNode);
        /*
        if (sNode.next != null) {
            pq.addAll(sNode.next.values());
        }*/
        while (!pq.isEmpty()) {
            Node topNode = pq.poll();
            //System.out.println("pq poll node character:" + topNode.ch);
            if (topNode.isKey() && topNode.value == topNode.best) {
                topNodeQueue.add(topNode);
                if (topNodeQueue.size() > 20) {
                    break;
                }
            }
            pq.addAll(topNode.next.values());
        }


        while (!topNodeQueue.isEmpty()) {
            StringBuilder temp = new StringBuilder();
            Node topNode = topNodeQueue.poll();
            while (topNode.parent != null) {
                temp.append(topNode.ch);
                topNode = topNode.parent;
            }
            keysList.add(temp.reverse().toString());
        }

        return keysList;
    }

}
