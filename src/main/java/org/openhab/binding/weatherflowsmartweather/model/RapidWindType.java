package org.openhab.binding.weatherflowsmartweather.model;

import com.google.gson.Gson;
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.eclipse.smarthome.core.types.ComplexType;
import org.eclipse.smarthome.core.types.PrimitiveType;

import javax.measure.quantity.Angle;
import javax.measure.quantity.Speed;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.SortedMap;
import java.util.TreeMap;

public class RapidWindType extends StringType implements ComplexType{
    long epoch;
    double speed;
    double direction;

    SortedMap<String, PrimitiveType> constituents = null;

    // This is not the most efficient code. Perhaps that's not a problem,
    // but we should try to improve it.
    public RapidWindType(EventRapidWindMessage value) {
        super(new Gson().toJson(value));

        epoch = new BigDecimal((double)value.getOb()[0]).longValue();
        speed = (double)(value.getOb()[1]);
        direction = (double)(value.getOb()[2]);
    }

    @Override
    public SortedMap<String, PrimitiveType> getConstituents() {
        if(constituents == null) {
            SortedMap<String,PrimitiveType> c = new TreeMap<>();

            c.put("epoch", new DecimalType(epoch));
            c.put("observationDate", new DateTimeType(Instant.ofEpochSecond(epoch).atZone(ZoneId.of("UTC"))));
            c.put("speed", new QuantityType<Speed>(speed, SIUnits.METRE_PER_SECOND));
            c.put("direction", new QuantityType<Angle>(direction, SIUnits.DEGREE_ANGLE));
        }

        return constituents;
    }
}
