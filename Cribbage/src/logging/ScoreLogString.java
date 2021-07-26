package logging;

import java.util.ArrayList;

public class ScoreLogString implements LogStringStrategy {
    @Override
    public String formLogString(ArrayList<String> info) {
        StringBuilder logString = new StringBuilder();
        for (int i = 0; i < info.size(); i++) {
            logString.append(info.get(i));
            if (i < info.size()-1) {
                logString.append(',');
            }
        }
        return logString.toString();
    }
}
