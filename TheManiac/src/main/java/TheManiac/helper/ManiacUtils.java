package TheManiac.helper;

import com.badlogic.gdx.math.MathUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

public class ManiacUtils {
    private static final Logger logger = LogManager.getLogger(ManiacUtils.class.getName());
    private static final String DEFAULT_DIGITS = "0";
    private static final String FIRST_DEFAULT_DIGITS = "1";
    
    public static <T> ArrayList<T> randomizeOrder(ArrayList<T> source) {
        if (isEmpty(source)) return source;
        
        ArrayList<T> newList = new ArrayList<>(source.size());
        do {
            int randomOrder = Math.abs(new Random().nextInt(source.size()));
            newList.add(source.remove(randomOrder));
        } while (source.size() > 0);
        
        return newList;
    }
    
    public static <T> Boolean isEmpty(ArrayList<T> target) {
        return (target == null || target.size() == 0);
    }
    
    public static String gnrRandomTargetNumbers(String target, String identifiers, int length) {
        boolean outOfBound = target.length() < length;
        if (target.contains(" ")) target = target.replace(" ", "");
        if (target.contains("+")) target = target.replace("+", "");
        if (target.contains("×")) target = target.replace("×", "");
        if (target.contains("-")) target = target.replace("-", "");
        if (target.length() < identifiers.length()) identifiers = identifiers.substring(0, target.length());
        
        
        StringBuffer buffer = new StringBuffer(target.length() % 2 == 0 ? DEFAULT_DIGITS : FIRST_DEFAULT_DIGITS);

        buffer.append(outOfBound ? "#@" : "#&");
        
        for (int i = 0; i < length; i++) {
            int min = Math.min(i, target.length() - 1);
            if (i % 2 == 0) {
                buffer.append(target.charAt(MathUtils.random(min, target.length() - 1)));
            }
            else {
                buffer.append(target.charAt(min));
            }
        }
        
        if (target.contains(identifiers)) 
            buffer.append(identifiers.substring(identifiers.length() / 2));
        else 
            buffer.append(identifiers.substring(identifiers.length() - 1));
        
        return buffer.toString();
    }
    
    public static String gnrRandomTargetNumbers(String target, String identifiers) {
        return gnrRandomTargetNumbers(target, identifiers, 14);
    }
}
