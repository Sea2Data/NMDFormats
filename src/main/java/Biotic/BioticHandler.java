/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Biotic;

import HierarchicalData.HierarchicalData;
import HierarchicalData.IO;
import XMLHandling.NamespaceFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 * @param <T> type for root elemement
 */
public abstract class BioticHandler <T extends HierarchicalData>{
    
    protected String latestBioticNamespace;
    protected Class<T> latestBioticClass;
    protected Set<String> compatibleNamespaces;
    
    public T readBiotic(InputStream xml) throws JAXBException, XMLStreamException, ParserConfigurationException, SAXException, IOException{
        if (this.compatibleNamespaces != null){
            return IO.parse(xml, this.latestBioticClass, new NamespaceFilter(this.latestBioticNamespace, this.compatibleNamespaces));
        }
        else{
            return IO.parse(xml, this.latestBioticClass);
        }
    }
    
    public T readBiotic(File xml) throws JAXBException, XMLStreamException, FileNotFoundException, ParserConfigurationException, SAXException, IOException{
        InputStream is = new FileInputStream(xml);
        T result =  this.readBiotic(is);
        is.close();
        return result;
    }
    
    public void saveBiotic(OutputStream stream, T data) throws JAXBException{
        IO.save(stream, data);
    }
    
}
