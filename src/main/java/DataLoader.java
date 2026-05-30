import java.io.*;
import java.util.*;


public class DataLoader {

    // userId -> (movieId -> rating)
    private final Map<String, Map<String, Double>> mainData   = new LinkedHashMap<>();
    private final Map<String, Map<String, Double>> targetData = new LinkedHashMap<>();

    // movieId -> film adı
    private final Map<String, String> movieNames = new LinkedHashMap<>();

    // main_data.csv başlık satırındaki film sütun sırası
    private final List<String> movieColumnIds = new ArrayList<>();


    //  Yükleyiciler


    public void loadMainData(String path) throws IOException {
        mainData.clear();
        movieColumnIds.clear();
        boolean header = true;

        for (String line : readLines(path)) {
            List<String> cols = parseLine(line);
            if (header) {
                for (int i = 1; i < cols.size(); i++) 
                    movieColumnIds.add(cols.get(i));
                header = false;
            } else {
                if (cols.size() < 2) 
                    continue;
                mainData.put(cols.get(0), buildRatingMap(cols, movieColumnIds));
            }
        }
    }

    public void loadTargetUsers(String path) throws IOException {
        targetData.clear();
        boolean header = true;
        List<String> targetCols = new ArrayList<>();

        for (String line : readLines(path)) {
            List<String> cols = parseLine(line);
            if (header) {
                for (int i = 1; i < cols.size(); i++) 
                    targetCols.add(cols.get(i));
                if (targetCols.isEmpty()) 
                    targetCols = new ArrayList<>(movieColumnIds);
                header = false;
            } else {
                if (cols.size() < 2) 
                    continue;
                targetData.put(cols.get(0), buildRatingMap(cols, targetCols));
            }
        }
    }

    public void loadMovies(String path) throws IOException {
        movieNames.clear();
        boolean header = true;

        for (String line : readLines(path)) {
            if (header) { 
                header = false; 
                continue;
            }
            List<String> cols = parseLine(line);
            if (cols.size() >= 2) 
                movieNames.put(cols.get(0), cols.get(1));
        }
    }


    //  Getter


    public Map<String, Map<String, Double>> getMainData()    { return mainData; }
    public Map<String, Map<String, Double>> getTargetData()  { return targetData; }
    public Map<String, String>              getMovieNames()  { return movieNames; }
    public List<String>                     getMovieColumnIds() { return movieColumnIds; }
    public List<String>                     getTargetUserIds()  { return new ArrayList<>(targetData.keySet()); }

    public String getMovieName(String movieId) {
        return movieNames.getOrDefault(movieId, null);
    }

    public boolean hasMovieName(String movieId) {
        return movieNames.containsKey(movieId);
    }


    //  Yardımcılar


    private Map<String, Double> buildRatingMap(List<String> cols, List<String> ids) {
        Map<String, Double> map = new LinkedHashMap<>();
        for (int i = 1; i < cols.size() && (i - 1) < ids.size(); i++)
            map.put(ids.get(i - 1), toDouble(cols.get(i)));
        return map;
    }

    private List<String> readLines(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) 
                    lines.add(line);
            }
        }
        return lines;
    }

    //virgülleri atlayan
    private List<String> parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '"')            inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) { 
                fields.add(sb.toString().trim()); 
                sb.setLength(0); 
            }
            else                     
                sb.append(c);
        }
        fields.add(sb.toString().trim());
        return fields;
    }

    private double toDouble(String s) {
        try { 
            return Double.parseDouble(s.trim()); 
        }
        catch (NumberFormatException e) { 
            return 0.0; 
        }
    }
}