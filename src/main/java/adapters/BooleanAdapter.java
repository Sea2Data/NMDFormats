/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author kjetilf
 */
public class BooleanAdapter extends XmlAdapter<String, Boolean> {

    @Override
    public Boolean unmarshal(String val) throws Exception {
        try {
            return new Boolean(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String marshal(Boolean val) throws Exception {
        if (val != null) {
            return val.toString();
        } else {
            return null;
        }
    }

}
