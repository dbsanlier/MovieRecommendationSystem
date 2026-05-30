import java.util.List;
import java.util.Map;

 //  cos(A, B) = (A · B) / (||A|| × ||B||)
 

public class CosineSimilarity {

    public static double compute(Map<String, Double> vecA,
                                 Map<String, Double> vecB,
                                 List<String> movieIds) {
        double dot = 0, normA = 0, normB = 0;

        for (String id : movieIds) {
            double a = vecA.getOrDefault(id, 0.0);
            double b = vecB.getOrDefault(id, 0.0);
            dot   += a * b;
            normA += a * a;
            normB += b * b;
        }

        if (normA == 0 || normB == 0) 
            return 0.0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
