package ar.edu.um.prog2.scalco.service;

import static org.junit.jupiter.api.Assertions.*;

//import static org.mockito.Mockito.when;

import ar.edu.um.prog2.scalco.IntegrationTest;
import ar.edu.um.prog2.scalco.repository.OrdenRepository;
import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
//import jakarta.transaction.Transactional;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:/config/application.yml")
public class AnalisisServiceIT {

    private static final Long id = 13l;

    private static final Integer cliente = 201311;

    private static final Integer accionId = 13;

    private static final String accion = "PAM";

    private static final String operacion = "VENTA";
    private static final String operacion2 = "COMPRA";

    private static final Float precio = 123.454f;

    private static final Integer cantidad = 6;
    private static final Integer cantidad1 = 76;

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
    public void horarioPermitido() {
        assertTrue(analisisService.horarioPermitido(modo, fechaOperacion));

        assertTrue(analisisService.horarioPermitido(modo1, fechaOperacion));

        assertTrue(analisisService.horarioPermitido(modo2, fechaOperacion));
    }

    @Test
    public void esPosibleProcesar() throws JsonProcessingException {
        orden.setOperacion(operacion2);
        assertFalse(analisisService.esPosibleProcesar(orden));
    }

    @Test
    public void NoEsPosibleProcesar() throws JsonProcessingException {
        orden.setCantidad(cantidad1);
        assertTrue(analisisService.esPosibleProcesar(orden));
    }
}
