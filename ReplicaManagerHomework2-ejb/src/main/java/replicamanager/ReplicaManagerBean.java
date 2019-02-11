/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replicamanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import util.Operation;

/**
 *
 * @author INGRAITI_MESSINA
 */
@Singleton
@Startup
public class ReplicaManagerBean implements ReplicaManagerBeanLocal {

    private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/homework?useUnicode=yes&characterEncoding=utf8&autoReconnect=true&verifyServerCertificate=false&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    
     // init connection object
    private Connection connection;
    // init properties object
    private Properties properties;

    // create properties
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
        }
        return properties;
    }
    
    
     // disconnect database
    private void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void connect() {
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                
            }
        }
    }
    
    /**
     * Visualizza tutti gli elementi del database
     * @return list 
     */
    @Override
    public List<Operation> getOperations(){
        
        ResultSet rs = null;
        Statement st = null;
        List<Operation> list = new ArrayList<>();
        
       
        try {
            this.connect();
            st = connection.createStatement();
            rs = st.executeQuery("SELECT * FROM operazioni");
            
            while (rs.next())
            {
                list.add( new Operation(
                        rs.getString("Name"),
                        rs.getFloat("Value")
                ));        
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReplicaManagerBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(rs != null) 
                try {
                    rs.close();
            } catch (SQLException ex) {
                
                Logger.getLogger(ReplicaManagerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(st != null)
               try {
                   st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ReplicaManagerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            this.disconnect();
        }
        return list;
    }
    
    /**
     * Visualizza tutti gli elementi 
     * @return list 
     */
    @Override
    public List<Operation> getOperation(String name) {
        ResultSet rs = null;
        Statement st = null;
        List<Operation> ret = new ArrayList<>();
        try {
            this.connect();
            st = connection.createStatement();
            rs = st.executeQuery("SELECT * FROM operazioni WHERE name = '" + name + "'");
            while(rs.next()) {
                ret.add(new Operation(
                        rs.getString("Name"),
                        rs.getFloat("Value")
                ));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ReplicaManagerBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ReplicaManagerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            if (st != null)
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ReplicaManagerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            this.disconnect();
        }
        return ret;
    }  
    
    @Override
    public List<Operation> getOrdered(String name) {
        ResultSet rs = null;
        Statement st = null;
        List<Operation> ret = new ArrayList<>();
        try {
            this.connect();
            st = connection.createStatement();
            rs = st.executeQuery("SELECT * FROM operazioni WHERE name = '" + name + "'" + "ORDER BY value ASC");
            while(rs.next()) {
                ret.add(new Operation(
                        rs.getString("Name"),
                        rs.getFloat("Value")
                ));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ReplicaManagerBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ReplicaManagerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            if (st != null)
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ReplicaManagerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            this.disconnect();
        }
        return ret;
    } 
    
    
    @Override
    public int addOp(Operation op) {
        ResultSet rs = null;
        Statement st = null;
        int ret = 0;
        try {
            this.connect();
            st = connection.createStatement();
            ret = st.executeUpdate("INSERT INTO operazioni (name, value) VALUES ('" +  op.getName()+ "','" + op.getValue() + "')");
        } catch (SQLException ex) {
            Logger.getLogger(ReplicaManagerBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (st != null)
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ReplicaManagerBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            this.disconnect();
        }
        return ret;
    }
    @Override
    public String sendAck(){
        return "STATUS_OK_200";     
    }
}
