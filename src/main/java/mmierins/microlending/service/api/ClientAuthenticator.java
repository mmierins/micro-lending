package mmierins.microlending.service.api;

import mmierins.microlending.domain.Client;

public interface ClientAuthenticator {

    Client getOrCreateNewClientByIP(String ipAddress);

}
