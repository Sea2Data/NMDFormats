/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

/**
 * Use for conversion issues not related to config files (missing source data. Undefined hard coded converisions etc).
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RDBESConversionException extends Exception {

    public RDBESConversionException(String msg) {
        super(msg);
    }
    
}
