/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Biotic.Biotic1;

import Biotic.BioticHandler;
import BioticTypes.v1_4.MissionsType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class Biotic1Handler extends BioticHandler<MissionsType> {

    public Biotic1Handler() {
        this.latestBioticNamespace = "http://www.imr.no/formats/nmdbiotic/v1.4";
        this.latestBioticClass = MissionsType.class;
        this.compatibleNamespaces = new HashSet<>();
        this.compatibleNamespaces.add("http://www.imr.no/formats/nmdbiotic/v1.3");
        this.compatibleNamespaces.add("http://www.imr.no/formats/nmdbiotic/v1.2");
        this.compatibleNamespaces.add("http://www.imr.no/formats/nmdbiotic/v1.1");
        this.compatibleNamespaces.add("http://www.imr.no/formats/nmdbiotic/v1");
    }
    
    @Override
    public MissionsType readBiotic(InputStream xml) throws JAXBException, XMLStreamException, ParserConfigurationException, SAXException, IOException{
        return super.readBiotic(xml);
    }
    
    
    @Override
    public void saveBiotic(OutputStream xml, MissionsType data) throws JAXBException{
        super.saveBiotic(xml, data);
    }
}
