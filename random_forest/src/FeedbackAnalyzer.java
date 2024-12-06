import java.util.*;

// This class analyzes the data and provides suggestions for improvement.
public class FeedbackAnalyzer {
    private Map<String,Double> passAvg;
    private Map<String,Double> failAvg;

    // These indices can be "improved" by the student.
    private final List<Integer> controllable = Arrays.asList(0,1,5,6,7,4,9,14);

    private final String[] featureNames = {
        "Hours Studied","Attendance","Parental Involvement","Access to Resources","Extracurricular Activities",
        "Sleep Hours","Previous Scores","Motivation Level","Internet Access","Tutoring Sessions",
        "Family Income","Teacher Quality","School Type","Peer Influence","Physical Activity",
        "Learning Disabilities","Parental Education Level","Distance from Home","Gender"
    };

    private final Map<Integer,String[]> catMap;

    public FeedbackAnalyzer(List<Node> data) {
        catMap = buildCatMap();
        calcAvgs(data);
    }

    public List<String> getSuggestions(double[] userInput, String result) {
        List<String> sugs=new ArrayList<>();
        sugs.add("Your Result: "+result);
        sugs.add("");
        sugs.add("Suggestions:");
        boolean hasSuggestions=false;
        for (int i:controllable) {
            double uv=userInput[i];
            double pav=passAvg.get("feature"+i);
            if (catMap.containsKey(i)) {
                String ucat=catMap.get(i)[(int)uv];
                String pcat=catMap.get(i)[(int)Math.round(pav)];
                if (!ucat.equals(pcat)) {
                    sugs.add("- Maybe change "+featureNames[i]+" from '"+ucat+"' to '"+pcat+"'.");
                    hasSuggestions=true;
                }
            } else {
                int ruv=(int)Math.round(uv);
                int rpav=(int)Math.round(pav);
                if (Math.abs(ruv-rpav)>2) {
                    if (ruv<rpav) {
                        sugs.add("- Consider improving "+featureNames[i]+" closer to "+rpav+".");
                    } else {
                        sugs.add("- Your "+featureNames[i]+" is good compared to avg passing students!");
                    }
                    hasSuggestions=true;
                }
            }
        }
        if(!hasSuggestions) {
            sugs.add("- No major changes needed.");
        }
        return sugs;
    }

    private void calcAvgs(List<Node> data) {
        int totalF=19;
        Map<String,Double> pSum=new HashMap<>();
        Map<String,Double> fSum=new HashMap<>();
        int pCount=0,fCount=0;
        for (int i=0;i<totalF;i++) {
            pSum.put("feature"+i,0.0);
            fSum.put("feature"+i,0.0);
        }

        for (Node d:data) {
            if (d.getLabel()==1) {
                pCount++;
                for (int i=0;i<totalF;i++)
                    pSum.put("feature"+i,pSum.get("feature"+i)+d.getFeature(i));
            } else {
                fCount++;
                for (int i=0;i<totalF;i++)
                    fSum.put("feature"+i,fSum.get("feature"+i)+d.getFeature(i));
            }
        }

        passAvg=new HashMap<>();
        failAvg=new HashMap<>();
        for (int i=0;i<totalF;i++) {
            passAvg.put("feature"+i,pCount>0?pSum.get("feature"+i)/pCount:0);
            failAvg.put("feature"+i,fCount>0?fSum.get("feature"+i)/fCount:0);
        }
    }

    private Map<Integer,String[]> buildCatMap() {
        Map<Integer,String[]> m=new HashMap<>();
        m.put(2,new String[]{"Low","Medium","High"});
        m.put(3,new String[]{"Low","Medium","High"});
        m.put(4,new String[]{"No","Yes"});
        m.put(7,new String[]{"Low","Medium","High"});
        m.put(8,new String[]{"No","Yes"});
        m.put(10,new String[]{"Low","Medium","High"});
        m.put(11,new String[]{"Low","Medium","High"});
        m.put(12,new String[]{"Public","Private"});
        m.put(13,new String[]{"Negative","Neutral","Positive"});
        m.put(15,new String[]{"No","Yes"});
        m.put(16,new String[]{"High School","College","Postgraduate"});
        m.put(17,new String[]{"Near","Moderate","Far"});
        m.put(18,new String[]{"Male","Female"});
        return m;
    }
}
