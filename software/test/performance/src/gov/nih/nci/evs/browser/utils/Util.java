package gov.nih.nci.evs.browser.utils;

import java.text.DecimalFormat;

import org.LexGrid.LexBIG.util.Prompt;

public class Util {
	public static final String SEPARATOR = 
		"----------------------------------------" +
		"----------------------------------------";
    private static DecimalFormat _doubleFormatter = new DecimalFormat("0.00");

    public static class StopWatch {
        private long _startMS = 0;
        
        public StopWatch() {
            start();
        }
    
        public void start() {
            _startMS = System.currentTimeMillis();
        }
        
        public long duration() {
            return System.currentTimeMillis() - _startMS;
        }
        
        public String getResult() {
            long time = duration();
            double timeSec = time/1000.0;
            double timeMin = timeSec/60.0;
            
            return "Run time: " + time + " ms, " + 
                _doubleFormatter.format(timeSec) + " sec, " + 
                _doubleFormatter.format(timeMin) + " min";
        }
    }
    
    public static String promptAlgorithm(String algorithm) {
        while (true) {
            algorithm = Prompt.prompt("algorithm", algorithm);
            
            if (algorithm.equalsIgnoreCase("exactMatch") ||
                    algorithm.equalsIgnoreCase("e")) {
                algorithm = "exactMatch";
                break;
            } else if (algorithm.equalsIgnoreCase("startsWith") ||
                    algorithm.equalsIgnoreCase("s")) {
                algorithm = "startsWith";
                break;
            } else if (algorithm.equalsIgnoreCase("contains") ||
                    algorithm.equalsIgnoreCase("c")) {
                algorithm = "contains";
                break;
            }
            System.out.println("  Valid values: exactMatch, startsWith, contains");
        }
        return algorithm;
    }
}