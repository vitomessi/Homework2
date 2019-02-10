/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replicamanager;

import java.util.List;
import javax.ejb.Local;
import util.Operation;

/**
 *
 * @author INGRAITI_MESSINA
 */
@Local
public interface ReplicaManagerBeanLocal {
    
    public List<Operation> getOperations();
    public List<Operation> getOperation(String name);
    public List<Operation> getOrdered(String name);
    public int addOp(Operation op);
}