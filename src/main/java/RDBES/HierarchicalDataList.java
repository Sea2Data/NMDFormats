/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDBES;

import HierarchicalData.HierarchicalData;
import java.util.List;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class HierarchicalDataList extends HierarchicalData{
    
    List<HierarchicalData> list;
    
    public HierarchicalDataList(List<HierarchicalData> list){
        this.list=list;
    }
    public List<HierarchicalData> getChildren() {
        return this.list;
    }
    
}
