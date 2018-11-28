/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;


import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
class TemporalStrata {

    List<TemporalStratum> strata;

    public TemporalStrata(List<TemporalStratum> strata) throws StrataException{
        this.strata=strata;
        for (TemporalStratum s: strata){
            for (TemporalStratum d: strata){
                if (s!=d && (s.overlap(d) || d.overlap(s))){
                    throw new StrataException("Temporal strata overlap in time");
                }
            }   
        }
    }
    
    public TemporalStratum getStratum(int day) throws StrataException{
        for (TemporalStratum s: this.strata){
            if (s.inStratum(day)){
                return s;
            }
        }
        
        throw new StrataException("Day " + day + " not in strata.");
    }
    
    public TemporalStratum getStratum(XMLGregorianCalendar date) throws StrataException{
        Calendar cday = date.toGregorianCalendar();
        Calendar fday = new GregorianCalendar();
        fday.set(date.getYear(), 0, 0);
        int day;
        day = (int) ChronoUnit.DAYS.between(fday.toInstant(), cday.toInstant());
        return( getStratum(day+1));
    }
    
}
