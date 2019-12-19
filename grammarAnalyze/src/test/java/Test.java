import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by BorisLiu on 2019/11/25
 */
public class Test {

    public static void main(String[] args) {
        String str = "@|sdh|fkjs|@|sjkhf";
        String[] split = str.split("[|]");
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }
        HashMap hashMap = new LinkedHashMap<>();

    }

}
