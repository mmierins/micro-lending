package mmierins.microlending.service.api;

import javax.servlet.http.HttpServletRequest;

public interface IPAddressResolver {

    String resolveIP(HttpServletRequest request);

}
