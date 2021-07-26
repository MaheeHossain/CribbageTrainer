package logging;
import java.util.ArrayList;

public interface LogStringStrategy {

    /* extract information from the ArrayList to form an appropriate log string */
    String formLogString(ArrayList<String> info);
}
