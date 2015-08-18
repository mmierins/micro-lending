package mmierins.microlending.misc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import mmierins.microlending.misc.MoneyUtils;

import java.io.IOException;
import java.math.BigDecimal;

public class MoneySerializer extends JsonSerializer<Long> {

    @Override
    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        long value2 = MoneyUtils.eurosFromCents(value);
        jgen.writeNumber(new BigDecimal(value2).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    }

}
