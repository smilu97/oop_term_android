package kr.ac.hanyang.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by gim-yeongjin on 2017. 5. 17..
 */

public class PhoneNumber extends RealmObject {
    String phoneNumber;

    public PhoneNumber() {
        this.phoneNumber = null;
    }

    public PhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
