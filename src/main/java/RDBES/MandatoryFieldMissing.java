/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class MandatoryFieldMissing extends RDBESConversionException {

    public MandatoryFieldMissing(String msg) {
        super(msg);
    }
    
}
