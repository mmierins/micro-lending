package mmierins.microlending.service.impl;

import mmierins.microlending.domain.Client;
import mmierins.microlending.repository.ClientRepository;
import mmierins.microlending.service.api.ClientAuthenticator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class ClientAuthenticatorImpl implements ClientAuthenticator {

    private Logger logger = Logger.getLogger(getClass());
    private ClientRepository clientRepository;

    @Autowired
    public ClientAuthenticatorImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public Client getOrCreateNewClientByIP(String ipAddress) {
        Client client = clientRepository.findByIp(ipAddress);

        if (client == null) {
            client = new Client();
            client.setIp(ipAddress);
            clientRepository.save(client);

            logger.info(
                String.format(
                        "Created new client with ID = %d with ip = %s",
                        client.getId(), ipAddress)
            );
        } else {
            logger.info(
                String.format(
                        "Retrieved client with ID = %d by ip = %s",
                        client.getId(), ipAddress)
            );
        }

        return client;
    }

}
