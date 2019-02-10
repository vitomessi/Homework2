/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import util.Operation;

/**
 *
 * @author INGRAITI_MESSINA
 */
@Path("operazioni")
@RequestScoped
public class RestApiRM {
    
    replicamanager.ReplicaManagerBeanLocal replicaManager = lookupReplicaManagerBeanLocal();
    
    
    @Context
    private UriInfo context;
    
    //constructor
    public RestApiRM() {
        
    }
    /**
     * get di tutti i valori
     * @return tutti i valori nel database
     */
    @GET
    @Path("/getAll")
    @Produces(MediaType.TEXT_PLAIN)
     public String getAll() {
         List<Operation> list = replicaManager.getOperations();
         ObjectMapper mapper = new ObjectMapper();
        try {
            String ret = mapper.writeValueAsString(list);
            return ret;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiRM.class.getName()).log(Level.SEVERE, null, ex);
        }
      return "[]";
     }
     
    /**
     * restituzione di tutti i valori che hanno un determinato nome
     * @param name
     * @return 
     */ 
    @GET
    @Path("/getOp/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getOp(@PathParam("name") String name)
    {
       ObjectMapper mapper = new ObjectMapper();
       
       try {
           return mapper.writeValueAsString(replicaManager.getOperation(name));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiRM.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       return "[]";
    }
    
    /**
     * restituzione ordinata di tutti i valori di una determinata operazione
     * @param name
     * @return tutte le operazioni di uno stesso tipo, ordinata per id
     */
    @GET
    @Path("/getOrd/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getOrdered(@PathParam("name") String name)
    {
       ObjectMapper mapper = new ObjectMapper();
       
       try {
           return mapper.writeValueAsString(replicaManager.getOrdered(name));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RestApiRM.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       return "[]";
    }
    
    @POST
    @Path("/add/{id}/{name}/{value}")
    @Consumes(MediaType.TEXT_PLAIN)
    public void add(@PathParam("id") String id, @PathParam("name") String name, @PathParam("value") float value){
        Operation op = new Operation(Integer.parseInt(id), name, value);
        replicaManager.addOp(op);
        
    }
    
    
    
    
    /* lookupReplicaManagerBeanLocal METHOD */
    private replicamanager.ReplicaManagerBeanLocal  lookupReplicaManagerBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (replicamanager.ReplicaManagerBeanLocal ) c.lookup("java:global/ReplicaManagerHomework2-ear/ReplicaManagerHomework2-ejb-1.0-SNAPSHOT/ReplicaManagerBean!replicamanager.ReplicaManagerBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
