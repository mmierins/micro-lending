package mmierins.microlending.service.impl;

import mmierins.microlending.service.api.IPAddressResolver;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IPAddressResolverTest {

    @Autowired
    private IPAddressResolver ipAddressResolver;

    @Before
    public void init() {
        ipAddressResolver = new IPAddressResolverImpl();
    }

    @Test
    public void returns_correct_ip_address_when_invoked() {
        String MOCKED_IP = "123.456.789.900";
        HttpServletRequest mockedRequest = mock(HttpServletRequest.class);
        when(mockedRequest.getRemoteAddr()).thenReturn(MOCKED_IP);
        assertEquals(MOCKED_IP, ipAddressResolver.resolveIP(mockedRequest));
    }

}
