package mmierins.microlending.repository;

import mmierins.microlending.domain.Parameter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ParameterRepository extends CrudRepository<Parameter, Long> {

    @Transactional(readOnly = true)
    Parameter findByKey(String key);

}
