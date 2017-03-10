package testclient;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class MyItem {

    private String desc;
    private double value;

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Number getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.desc + "= " + this.value;
    }
}

public class jsonconversiontest {

    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();
        MyItem i = new MyItem();
        i.setDesc("Test");
        i.setValue(0.165352);
        String s = null;

        try {
            s = mapper.writeValueAsString(i);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("JAVA to JSON: " + s);

        MyItem i2 = null;
        try {
            i2 = mapper.readValue(s, MyItem.class);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\nJSON to JAVA: " + i2);
        System.out.println("\n\"value\" field type: : " + i2.getValue().getClass());
    }

}