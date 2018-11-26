/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

/**
 * Use when info needed to identify record in either data format is missing
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class MissingKeyException extends RDBESConversionException {

    public MissingKeyException(String msg) {
        super(msg);
    }
    
}
