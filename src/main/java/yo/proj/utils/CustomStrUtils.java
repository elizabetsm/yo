package yo.proj.utils;

import java.util.Arrays;
import java.util.List;

public class CustomStrUtils {

    public static List<String> getListUserFriends(String usrStr){
         List<String> strList = Arrays.asList(usrStr.split("\\s*@\\s*"));
         return strList.subList(1, strList.size());
    }
}
