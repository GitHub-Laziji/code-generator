package pg.laziji.generator.mybatis.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TemplateUtils {

    public static String time(String format){
        return new SimpleDateFormat(format).format(new Date());
    }

    public static String time(){
        return DateFormat.getDateTimeInstance().format(new Date());
    }
}
