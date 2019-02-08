/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author INGRAITI_MESSINA
 */
public class Operation implements Serializable{
    
    private int id;
    private String name;
    private float value;

    public Operation(int id, String name, float value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Operation{" + "id=" + id + ", name=" + name + ", value=" + value + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + Float.floatToIntBits(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Operation other = (Operation) obj;
        if (this.id != other.id) {
            return false;
        }
        if (Float.floatToIntBits(this.value) != Float.floatToIntBits(other.value)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
    
}

