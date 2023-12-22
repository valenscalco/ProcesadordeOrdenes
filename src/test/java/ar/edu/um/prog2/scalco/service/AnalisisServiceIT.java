package ar.edu.um.prog2.scalco.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ar.edu.um.prog2.scalco.IntegrationTest;
import ar.edu.um.prog2.scalco.repository.OrdenRepository;
import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import jakarta.transaction.Transactional;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.DateTimeProvider;

@IntegrationTest
@Transactional
public class AnalisisServiceIT {

    private static final Long id = 13l;

    private static final Integer cliente = 201311;

    private static final Integer accionId = 13;

    private static final String accion = "PAM";

    private static final String operacion = "VENTA";

    private static final Float precio = 123.454f;

    private static final Integer cantidad = 6;

    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    public static final ZonedDateTime fechaOperacion = ZonedDateTime.of(2023, 12, 12, 12, 12, 12, 12, ZoneId.of("UTC+1"));

    public static final String modo = "AHORA";

    private static final String modo1 = "FINDIA";

    private static final String modo2 = "PRINCIPIODIA";

    private static final Boolean operacionExitosa = null;
    //private static final Boolean operacionNoExitosa = false;

    private static final String operacionObservaciones = null;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private AnalisisService analisisService;

    @MockBean
    private DateTimeProvider dateTimeProvider;

    private OrdenDTO orden;

    @BeforeEach
    public void init() {
        orden = new OrdenDTO();
        orden.setId(id);
        orden.setCliente(cliente);
        orden.setAccionId(accionId);
        orden.setAccion(accion);
        orden.setOperacion(operacion);
        orden.setModo(modo);
        orden.setPrecio(precio);
        orden.setCantidad(cantidad);
        orden.setOperacionExitosa(operacionExitosa);
        orden.setFechaOperacion(fechaOperacion);
        orden.setOperacionObservaciones(operacionObservaciones);
    }

    @Test
    public void is1equalsto1() {
        assertTrue(1 == 1);
    }

    @Test
    @Transactional
    public void horarioPermitido() {
        Boolean result = analisisService.horarioPermitido(modo, fechaOperacion, orden);
        assertThat(result).isEqualTo(true);

        Boolean result1 = analisisService.horarioPermitido(modo1, fechaOperacion, orden);
        assertThat(result1).isEqualTo(true);

        Boolean result2 = analisisService.horarioPermitido(modo2, fechaOperacion, orden);
        assertThat(result2).isEqualTo(true);
    }
}
