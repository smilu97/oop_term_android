package kr.ac.hanyang.realm;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gim-yeongjin on 2017. 5. 17..
 */

public class Call extends RealmObject{
    String from;
    String to;
    Date date;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String description () {
        String format = "from: %s\n" +
                "to: %s\n" +
                "date: %s";
        Realm realm = Realm.getDefaultInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format(format, from, to, sdf.format(date));
    }
}
