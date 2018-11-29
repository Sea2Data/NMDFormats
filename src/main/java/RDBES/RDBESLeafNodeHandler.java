/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import HierarchicalData.RelationalConversion.ILeafNodeHandler;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RDBESLeafNodeHandler implements ILeafNodeHandler{
    
    @Override
    public String extractValue(Object node) throws ClassCastException {
        if (node == null){
            return "";
        }
        if (node instanceof String){
            return (String)node;
        }
        if (node instanceof Integer){
            return node.toString();
        }
        if (node instanceof Double){
            return node.toString();
        }
        if (node instanceof Boolean){
            return node.toString();
        }
        if (node instanceof XMLGregorianCalendarImpl){
            return node.toString();
        }

        throw new UnsupportedOperationException("Type " + node.getClass().getSimpleName() + " not implemented");
    }

    @Override
    public Set getLeafNodeComplexTypes() {
        Set ln = new HashSet();
        return ln;
    }
}
