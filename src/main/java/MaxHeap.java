/**
 * Max heap 
 * Kök en yüksek benzerlik skoru
 *
 * insert: O(log n)
 * extractMax: O(log n)
 * findNode: O(log n) — konuma binary gösterim ile gidilir
 */
public class MaxHeap {

    private HeapNode root;
    private int      size;

    public MaxHeap() {
        this.root = null;
        this.size = 0;
    }



    /**
     * 0= sol, 1= sağ 
     
     */
    private HeapNode findNode(int pos) {
        String bits = Integer.toBinaryString(pos);
        HeapNode curr = root;
        for (int i = 1; i < bits.length(); i++) {
            curr = (bits.charAt(i) == '0') ? curr.left : curr.right;
        }
        return curr;
    }

  

    public void insert(String userId, double similarity) {
        HeapNode node = new HeapNode(userId, similarity);
        size++;

        if (root == null) {
            root = node;
            return;
        }

        HeapNode parent = findNode(size / 2);
        node.parent = parent;

        if (size % 2 == 0) 
            parent.left  = node;
        else               
            parent.right = node;

        heapifyUp(node);
    }

    private void heapifyUp(HeapNode node) {
        while (node.parent != null
               && node.getSimilarity() > node.parent.getSimilarity()) {
            swapData(node, node.parent);
            node = node.parent;
        }
    }

   

    public HeapNode extractMax() {
        if (root == null) return null;

        HeapNode result = new HeapNode(root.getUserId(), root.getSimilarity());

        if (size == 1) {
            root = null;
            size = 0;
            return result;
        }

        HeapNode last = findNode(size);

        root.setUserId(last.getUserId());
        root.setSimilarity(last.getSimilarity());

        if (last.parent.right == last) 
            last.parent.right = null;
        else                           
            last.parent.left  = null;
        last.parent = null;
        size--;

        heapifyDown(root);
        return result;
    }

    private void heapifyDown(HeapNode node) {
        while (true) {
            HeapNode largest = node;
            if (node.left  != null && node.left.getSimilarity()  > largest.getSimilarity()) largest = node.left;
            if (node.right != null && node.right.getSimilarity() > largest.getSimilarity()) largest = node.right;
            if (largest == node) break;
            swapData(node, largest);
            node = largest;
        }
    }

    

    private void swapData(HeapNode a, HeapNode b) {
        String tmpId  = a.getUserId();   
        double tmpSim = a.getSimilarity();
        a.setUserId(b.getUserId());      
        a.setSimilarity(b.getSimilarity());
        b.setUserId(tmpId);              
        b.setSimilarity(tmpSim);
    }

    public boolean isEmpty()  { return size == 0; }
    public int     getSize()  { return size; }

    public void clear() {
        root = null;
        size = 0;
    }
}
