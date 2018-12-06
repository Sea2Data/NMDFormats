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
class FishWeight {
    
    String aphia;
    Double weight;
    
    public FishWeight(String aphia, double weight){
        this.aphia = aphia;
        this.weight = weight;
    }

    public String getAphia() {
        return aphia;
    }

    public Double getWeight() {
        return weight;
    }
    
}
