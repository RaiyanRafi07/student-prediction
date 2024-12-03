import java.io.*;
import java.util.*;

public class DataLoader {
    private static String[] featureNames;

    public static List<DataPoint> loadData(String filePath) {
        List<DataPoint> data = new ArrayList<>();

        // Define mappings for categorical variables
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Low", 0);
        mapping.put("Medium", 1);
        mapping.put("High", 2);
        mapping.put("No", 0);
        mapping.put("Yes", 1);
        mapping.put("Public", 0);
        mapping.put("Private", 1);
        mapping.put("Negative", 0);
        mapping.put("Neutral", 1);
        mapping.put("Positive", 2);
        mapping.put("High School", 0);
        mapping.put("College", 1);
        mapping.put("Postgraduate", 2);
        mapping.put("Near", 0);
        mapping.put("Moderate", 1);
        mapping.put("Far", 2);
        mapping.put("Male", 0);
        mapping.put("Female", 1);

        List<String[]> records = parseCSV(filePath);

        if (records.isEmpty()) {
            System.err.println("The dataset file is empty or could not be read.");
            return data;
        }

        String[] headers = records.get(0);
        featureNames = Arrays.copyOf(headers, headers.length - 1);
        int numFeatures = headers.length - 1; // Exclude the label column

        for (int i = 1; i < records.size(); i++) {
            String[] tokens = records.get(i);
            if (tokens.length != headers.length) {
                System.err.println("Mismatch in the number of columns at line " + (i + 1));
                continue;
            }

            double[] features = new double[numFeatures];
            boolean validDataPoint = true;

            for (int j = 0; j < numFeatures; j++) {
                String value = tokens[j].trim();

                if (value.isEmpty()) {
                    System.err.println("Missing value at line " + (i + 1) + ", column " + (j + 1));
                    validDataPoint = false;
                    break; // Skip this data point
                }

                if (mapping.containsKey(value)) {
                    features[j] = mapping.get(value);
                } else {
                    try {
                        features[j] = Double.parseDouble(value);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format at line " + (i + 1) + ", column " + (j + 1) + ": '" + value + "'");
                        validDataPoint = false;
                        break; // Skip this data point
                    }
                }
            }

            if (!validDataPoint) {
                continue; // Skip this data point due to invalid value
            }

            // Handle the label (Pass/Fail)
            String examScoreStr = tokens[numFeatures].trim();
            double examScore;
            try {
                examScore = Double.parseDouble(examScoreStr);
            } catch (NumberFormatException e) {
                System.err.println("Invalid exam score at line " + (i + 1));
                continue; // Skip this data point
            }

            // Define passing threshold (e.g., 70)
            int label = examScore >= 70 ? 1 : 0; // 1 for passing, 0 for failing

            data.add(new DataPoint(features, label));
        }

        return data;
    }

    public static String[] getFeatureNames() {
        return featureNames;
    }

    // Custom CSV parser
    public static List<String[]> parseCSV(String filePath) {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean inQuotes = false;
            StringBuilder sb = new StringBuilder();
            List<String> tokens = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                tokens.clear();
                sb.setLength(0);
                inQuotes = false;

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
                records.add(tokens.toArray(new String[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
