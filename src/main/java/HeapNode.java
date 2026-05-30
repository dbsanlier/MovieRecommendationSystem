
 //Her düğüm bir kullanıcı ID
//hedef kullanıcıya olan kosinüs benzerlik
 
public class HeapNode {

    private String userId;
    private double similarity;

    HeapNode left;
    HeapNode right;
    HeapNode parent;

    public HeapNode(String userId, double similarity) {
        this.userId     = userId;
        this.similarity = similarity;
        this.left       = null;
        this.right      = null;
        this.parent     = null;
    }

    public String getUserId()              { return userId; }
    public void   setUserId(String id)     { this.userId = id; }
    public double getSimilarity()          { return similarity; }
    public void   setSimilarity(double s)  { this.similarity = s; }
}
