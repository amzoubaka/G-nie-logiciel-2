package com.pharmasys.services;

import java.sql.SQLException;
import java.util.List;

import com.pharmasys.dao.ClientDAO;
import com.pharmasys.models.Client;
import com.pharmasys.utils.SessionManager;
import com.pharmasys.utils.ValidationException;
import com.pharmasys.utils.ValidationUtil;

/**
 * Service de gestion des clients
 */
public class ClientService {
    private final ClientDAO clientDAO;
    private final LogService logService;
    
    public ClientService() {
        this.clientDAO = new ClientDAO();
        this.logService = new LogService();
    }
    
    /**
     * Crée un nouveau client
     */
    public int createClient(Client client) throws ValidationException, SQLException {
        ValidationUtil.validateClient(client);
        
        int clientId = clientDAO.insert(client);
        
        logService.log(SessionManager.getCurrentUser().getId(), "CREATE_CLIENT",
                      "Client créé: " + client.getFullName());
        
        return clientId;
    }
    
    /**
     * Met à jour un client
     */
    public void updateClient(Client client) throws ValidationException, SQLException {
        ValidationUtil.validateClient(client);
        
        clientDAO.update(client);
        
        logService.log(SessionManager.getCurrentUser().getId(), "UPDATE_CLIENT",
                      "Client modifié: " + client.getFullName());
    }
    
    /**
     * Recherche des clients
     */
    public List<Client> searchClients(String query) throws SQLException {
        if (query == null || query.trim().isEmpty()) {
            return clientDAO.findAll();
        }
        return clientDAO.search(query);
    }
    
    /**
     * Retourne tous les clients
     */
    public List<Client> getAllClients() throws SQLException {
        return clientDAO.findAll();
    }
    
    /**
     * Trouve un client par ID
     */
    public Client getClientById(int clientId) throws SQLException {
        return clientDAO.findById(clientId);
    }
}
