package mmierins.microlending.service.impl;

import mmierins.microlending.service.api.IPAddressResolver;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class IPAddressResolverImpl implements IPAddressResolver {

    private Logger logger = Logger.getLogger(getClass());

    @Override
    public String resolveIP(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        logger.info(String.format("Determined client IP as %s", ip));
        return ip;
    }

}
