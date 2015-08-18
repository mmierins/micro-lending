package mmierins.microlending.service.impl;

import mmierins.microlending.domain.Client;
import mmierins.microlending.repository.ClientRepository;
import mmierins.microlending.service.api.ClientAuthenticator;
import org.junit.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.*;

public class ClientAuthenticatorTest {

    ClientAuthenticator clientAuthenticator;

    @Test
    public void creates_new_client_when_no_existing_client_found() {
        Client client = new Client();
        client.setIp("ip1");

        ClientRepository clientRepository = mock(ClientRepository.class);
        when(clientRepository.findByIp("ip1")).thenReturn(null);
        clientAuthenticator = new ClientAuthenticatorImpl(clientRepository);
        Client otherClient = clientAuthenticator.getOrCreateNewClientByIP("ip1");
        assertNotNull(otherClient);
        verify(clientRepository).findByIp("ip1");
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    public void returns_existing_client_when_client_exists() {
        Client client = new Client();
        client.setIp("ip1");

        ClientRepository clientRepository = mock(ClientRepository.class);
        when(clientRepository.findByIp("ip1")).thenReturn(client);

        clientAuthenticator = new ClientAuthenticatorImpl(clientRepository);
        Client otherClient = clientAuthenticator.getOrCreateNewClientByIP("ip1");
        verify(clientRepository).findByIp("ip1");
        verifyNoMoreInteractions(clientRepository);
    }

}
