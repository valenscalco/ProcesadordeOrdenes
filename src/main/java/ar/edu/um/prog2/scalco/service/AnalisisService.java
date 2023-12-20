package ar.edu.um.prog2.scalco.service;

import ar.edu.um.prog2.scalco.repository.OrdenRepository;
import ar.edu.um.prog2.scalco.service.ExternalService;
import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import ar.edu.um.prog2.scalco.service.mapper.OrdenMapper;
import ar.edu.um.prog2.scalco.web.rest.OrdenResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AnalisisService {

    List<OrdenDTO> ordenesFallidas = new ArrayList<>();
    List<OrdenDTO> ordenesExitosas = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(AnalisisService.class);
    private final ExternalService externalService;
    private final OrdenService ordenService;

    public AnalisisService(ExternalService externalService, OrdenService ordenService) {
        this.externalService = externalService;
        this.ordenService = ordenService;
    }

    public void analizarOrden(OrdenDTO ordenDTO) throws JsonProcessingException {
        // Verificar si es posible procesar la orden
        if (esPosibleProcesar(ordenDTO)) {
            // Almacenar el resultado de la orden
            almacenarResultado(ordenDTO, true);
            log.info("orden aregada a lista analisis ordenes validas : {}", ordenDTO);
        } else {
            // Almacenar la orden en la lista de órdenes fallidas
            ordenDTO.setOperacionExitosa(false);
            almacenarResultado(ordenDTO, false);
            log.info("orden aregada a lista analisis ordenes no validas : {}", ordenDTO);
        }
    }

    private boolean esPosibleProcesar(OrdenDTO ordenDTO) throws JsonProcessingException {
        // Lógica para verificar condiciones de procesamiento
        log.info("verificando validez de la orden: {}", ordenDTO);
        if (ordenDTO.getOperacion().equals("VENTA")) {
            Integer cantidadDisponible = externalService.getClientesAccion(ordenDTO).getCantidadActual();
            if (cantidadDisponible == null || cantidadDisponible < ordenDTO.getCantidad()) {
                log.info("CANTIDAD DISPONIBLE INSUFICIENTE: {}", ordenDTO);
                ordenDTO.setOperacionObservaciones("Cantidad disponible insuficiente para la venta");
                return false;
            }
        }

        return (
            ordenDTO.getCantidad() > 0 &&
            horarioPermitido(ordenDTO.getModo(), ordenDTO.getFechaOperacion()) &&
            clienteAccionValido(ordenDTO.getCliente(), ordenDTO.getAccionId())
            //externalService.clientAndAccionExists(ordenDTO.getCliente(), ordenDTO.getAccionId())
        );
    }

    // Método para verificar si una orden es instantánea
    private boolean horarioPermitido(String modo, ZonedDateTime fechaOperacion) {
        // Lógica para verificar el horario permitido según el modo de la orden
        if ("AHORA".equals(modo) && esHorarioTransacciones(fechaOperacion)) {
            return true;
        } else if ("PRINCIPIODIA".equals(modo) || "FINDIA".equals(modo)) {
            return true;
        }
        return false;
    }

    private boolean clienteAccionValido(Integer cliente, Integer accionId) {
        // Lógica para verificar el horario permitido según el modo de la orden
        if (externalService.clientAndAccionExists(cliente, accionId)) {
            return true;
        }
        return false;
    }

    private boolean esHorarioTransacciones(ZonedDateTime fechaOperacion) {
        // Lógica para verificar si estamos dentro del horario permitido
        Integer inicio = 9;
        Integer fin = 18;
        return fechaOperacion.getHour() >= inicio && fechaOperacion.getHour() <= fin;
    }

    private void ejecutarOperacion(OrdenDTO ordenDTO) {
        ordenDTO.setOperacionExitosa(true);
        ordenDTO.setOperacionObservaciones("ok");
        log.info("Orden procesada con Exito");
    }

    private void almacenarResultado(OrdenDTO ordenDTO, boolean exitoso) {
        // Lógica para almacenar el resultado en la base de datos local
        if (exitoso) {
            ordenesExitosas.add(ordenDTO);
        } else {
            ordenesFallidas.add(ordenDTO);
        }
    }

    public void analizarNoProcesadas(String modo) throws JsonProcessingException {
        List<OrdenDTO> recoveredList = ordenService.findAll();

        for (OrdenDTO ordenDTO : recoveredList) {
            if (ordenDTO.getOperacionExitosa() == null) {
                analizarOrden(ordenDTO);
            }
        }

        for (OrdenDTO ordenDTO : ordenesExitosas) {
            if (ordenDTO.getModo() == modo) {
                ejecutarOperacion(ordenDTO);
                ordenService.save(ordenDTO);
            }
        }

        for (OrdenDTO ordenDTO : ordenesFallidas) {
            ordenService.save(ordenDTO);
        }

        ordenesFallidas = new ArrayList<>();
        ordenesExitosas = new ArrayList<>();
    }
}
