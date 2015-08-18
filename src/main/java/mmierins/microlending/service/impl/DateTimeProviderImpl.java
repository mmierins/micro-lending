package mmierins.microlending.service.impl;

import mmierins.microlending.service.api.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateTimeProviderImpl implements DateTimeProvider {

    @Override
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

}
