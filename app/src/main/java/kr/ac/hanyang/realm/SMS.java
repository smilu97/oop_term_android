package kr.ac.hanyang.realm;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gim-yeongjin on 2017. 5. 17..
 */

public class SMS extends RealmObject {
    String from;
    String to;
    String content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
                "content: %s\n" +
                "date: %s";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format(format, from, to, content, sdf.format(date));
    }
}
