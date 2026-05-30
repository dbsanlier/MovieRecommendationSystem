import java.util.*;

public class RecommendationEngine {

    private final DataLoader dataLoader;

    public RecommendationEngine(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public List<String> recommendByTargetUser(String targetUserId, int X, int K) {
        Map<String, Double> vec = dataLoader.getTargetData().get(targetUserId);
        if (vec == null) return List.of("User not found: " + targetUserId);
        return compute(vec, X, K);
    }

    public List<String> recommendByRatings(Map<String, Double> userVector, int X, int K) {
        return compute(userVector, X, K);
    }

    private List<String> compute(Map<String, Double> targetVec, int X, int K) {
        List<String> movieIds = dataLoader.getMovieColumnIds();

        // Heape tüm kullanıcıları ekleme
        MaxHeap heap = new MaxHeap();
        for (Map.Entry<String, Map<String, Double>> e : dataLoader.getMainData().entrySet()) {
            double sim = CosineSimilarity.compute(targetVec, e.getValue(), movieIds);
            heap.insert(e.getKey(), sim);
        }

        List<String> result = new ArrayList<>();
        int usersCompleted = 0;

        // Tam K film verebilen X kullanıcı 
        while (!heap.isEmpty() && usersCompleted < X) {
            HeapNode top = heap.extractMax();
            Map<String, Double> ratings = dataLoader.getMainData().get(top.getUserId());
            if (ratings == null) 
                continue;

            
            List<Map.Entry<String, Double>> sorted = new ArrayList<>();
            for (Map.Entry<String, Double> m : ratings.entrySet()) {
                if (m.getValue() > 0 && dataLoader.getMovieName(m.getKey()) != null) {
                    sorted.add(m);
                }
            }
            sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

            
            if (sorted.size() < K) 
                continue;

            // İlk K filmi al
            for (int i = 0; i < K; i++) {
                result.add(dataLoader.getMovieName(sorted.get(i).getKey()));
            }
            usersCompleted++;
        }

        return result;
    }
}