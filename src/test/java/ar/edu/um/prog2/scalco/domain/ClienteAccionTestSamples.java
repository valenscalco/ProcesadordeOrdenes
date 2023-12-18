package ar.edu.um.prog2.scalco.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteAccionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ClienteAccion getClienteAccionSample1() {
        return new ClienteAccion().id(1L).cliente(1).accionId(1).accion("accion1").cantidadActual(1).observaciones("observaciones1");
    }

    public static ClienteAccion getClienteAccionSample2() {
        return new ClienteAccion().id(2L).cliente(2).accionId(2).accion("accion2").cantidadActual(2).observaciones("observaciones2");
    }

    public static ClienteAccion getClienteAccionRandomSampleGenerator() {
        return new ClienteAccion()
            .id(longCount.incrementAndGet())
            .cliente(intCount.incrementAndGet())
            .accionId(intCount.incrementAndGet())
            .accion(UUID.randomUUID().toString())
            .cantidadActual(intCount.incrementAndGet())
            .observaciones(UUID.randomUUID().toString());
    }
}
