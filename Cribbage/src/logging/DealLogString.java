package logging;

import java.util.ArrayList;

public class DealLogString implements LogStringStrategy {
    @Override
    public String formLogString(ArrayList<String> info) {
        return info.get(0)+"," + info.get(1) +"," + info.get(2);
    }
}
