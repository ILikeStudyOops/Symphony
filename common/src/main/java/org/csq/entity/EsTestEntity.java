package org.csq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/*@Data
@AllArgsConstructor*/
public class EsTestEntity implements Serializable {
    private String name;
    private String sex;
    private String tel;
    public EsTestEntity(String name,
                        String sex,
                        String tel){
        this.name = name;
        this.sex = sex;
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
