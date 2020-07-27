package Tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetDate {

    public static String time(){


        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(date);

    }

}
