package logging;

import java.util.ArrayList;

public class PlayerInitialisationLogString implements LogStringStrategy {
    @Override
    public String formLogString(ArrayList<String> info) {
        return info.get(0) + ',' + info.get(1);
    }
}
