import java.io.*;
import java.util.*;

// This class loads data from a CSV file, parses it, and returns a list of Nodes.
// It also tracks how many lines were skipped due to errors.
public class DataLoader {
    private static String[] featureNames;

    public static class LoadResult {
        public List<Node> data;
        public int linesSkipped;
        public LoadResult(List<Node> data, int linesSkipped) {
            this.data = data;
            this.linesSkipped = linesSkipped;
        }
    }

    public static LoadResult loadData(String filePath) {
        List<Node> data = new ArrayList<>();
        int linesSkipped = 0;

        Map<String, Integer> mapping = buildMapping();

        List<String[]> records = parseCSV(filePath);
        if (records.isEmpty()) {
            return new LoadResult(data, linesSkipped);
        }

        String[] headers = records.get(0);
        featureNames = Arrays.copyOf(headers, headers.length - 1);
        int nf = headers.length - 1;

        // Start from line 1 since line 0 is headers
        for (int i = 1; i < records.size(); i++) {
            String[] tokens = records.get(i);
            if (tokens.length != headers.length) {
                linesSkipped++;
                continue;
            }

            double[] feats = new double[nf];
            boolean valid = true;

            // Parse features
            for (int j = 0; j < nf; j++) {
                String val = tokens[j].trim();
                if (val.isEmpty()) {
                    valid = false;
                    break;
                }
                if (mapping.containsKey(val)) {
                    feats[j] = mapping.get(val);
                } else {
                    try {
                        feats[j] = Double.parseDouble(val);
                    } catch (NumberFormatException e) {
                        valid = false;
                        break;
                    }
                }
            }

            if (!valid) {
                linesSkipped++;
                continue;
            }

            // Parse label
            String examStr = tokens[nf].trim();
            double examScore;
            try {
                examScore = Double.parseDouble(examStr);
            } catch (NumberFormatException e) {
                linesSkipped++;
                continue;
            }

            int label = examScore >= 70 ? 1 : 0;
            data.add(new Node(feats, label));
        }

        return new LoadResult(data, linesSkipped);
    }

    public static String[] getFeatureNames() {
        return featureNames;
    }

    private static Map<String, Integer> buildMapping() {
        Map<String, Integer> m = new HashMap<>();
        m.put("Low", 0);
        m.put("Medium", 1);
        m.put("High", 2);
        m.put("No", 0);
        m.put("Yes", 1);
        m.put("Public", 0);
        m.put("Private", 1);
        m.put("Negative", 0);
        m.put("Neutral", 1);
        m.put("Positive", 2);
        m.put("High School", 0);
        m.put("College", 1);
        m.put("Postgraduate", 2);
        m.put("Near", 0);
        m.put("Moderate", 1);
        m.put("Far", 2);
        m.put("Male", 0);
        m.put("Female", 1);
        return m;
    }

    // Simple CSV parser that respects quotes and commas.
    private static List<String[]> parseCSV(String filePath) {
        List<String[]> recs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> tokens = new ArrayList<>();
                boolean inQuotes = false;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c == '"') {
                        inQuotes = !inQuotes;
                    } else if (c == ',' && !inQuotes) {
                        tokens.add(sb.toString());
                        sb.setLength(0);
                    } else {
                        sb.append(c);
                    }
                }
                tokens.add(sb.toString());
                recs.add(tokens.toArray(new String[0]));
            }
        } catch (IOException e) {
            // If file can't be read, we just return what we have.
        }
        return recs;
    }
}
