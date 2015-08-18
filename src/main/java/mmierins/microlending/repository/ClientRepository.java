package mmierins.microlending.repository;

import mmierins.microlending.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Transactional(readOnly = true)
    Client findByIp(String ip);

}
