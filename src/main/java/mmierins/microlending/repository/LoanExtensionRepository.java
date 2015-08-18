package mmierins.microlending.repository;

import mmierins.microlending.domain.LoanExtension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanExtensionRepository extends JpaRepository<LoanExtension, Long> {

}
