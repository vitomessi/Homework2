/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
     * send an ack to ReaderWriter
     * @return 
     */
    @GET
    @Path("/sendAck") 
    @Produces(MediaType.TEXT_PLAIN)
    public String sendAck() {
        return  replicaManager.sendAck();
    }
    /**
     * Get of all elements of rm
     * @return 
     */
    @GET
    @Path("/all")
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
     * Get the elements of name {name} 
     * @param name
     * @return all the elements of name {name}
     */ 
    @GET
    @Path("/op/{name}")
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
     * Get the elements of name {name} ordered by insert
     * @param name
     * @return all elements of name {name} ordered by insert
     */
    @GET
    @Path("/ord/{name}")
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
    
    /**
     * Insert new element
     * @param name
     * @param value 
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.TEXT_PLAIN)
    public void add(String u){
        
       // Operation op = new Operation(new Gson,value);
       replicaManager.printLog(u);
       //replicaManager.sendAck();
  
       Operation op = new Gson().fromJson(u, Operation.class);
   
        replicaManager.addOp(op);
     
    }
    /**
     * Receive an abort from writer
     * @param log 
     */
    @POST
    @Path("/abort")
    @Consumes(MediaType.TEXT_PLAIN)
    public void sendAbort(String log)
    {
      boolean flag;
      flag = replicaManager.printLog(log);
      flag = false;
       
    }
    
    
    /**
     * add statistics in a log file
     * @param u
     * @return 
     */
    @POST
    @Path("/addLog")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response addLog(String u){
        boolean result = replicaManager.printLog(u);    
        return Response.status(200).entity(result).build();
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
