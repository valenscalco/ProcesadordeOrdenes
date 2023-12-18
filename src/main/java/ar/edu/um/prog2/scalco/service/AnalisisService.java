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

    public AnalisisService(ExternalService externalService) {
        this.externalService = externalService;
    }

    public void analizarOrden(OrdenDTO ordenDTO, boolean forceProcess) throws JsonProcessingException {
        // Verificar si es posible procesar la orden
        if (esPosibleProcesar(ordenDTO)) {
            if (ordenDTO.getModo().equals("AHORA") || forceProcess) {
                // Ejecutar la operación de compra o venta
                ejecutarOperacion(ordenDTO);
            } else {
                // Almacenar el resultado de la orden
                almacenarResultado(ordenDTO, true);
            }
        } else {
            // Almacenar la orden en la lista de órdenes fallidas
            almacenarResultado(ordenDTO, false);
        }
        log.info("lista analisis ordenes validas : {}", ordenesExitosas);
        log.info("lista analisis ordenes no validas : {}", ordenesFallidas);
    }

    private boolean esPosibleProcesar(OrdenDTO ordenDTO) throws JsonProcessingException {
        // Lógica para verificar condiciones de procesamiento
        log.info("verificando validez de la orden: {}", ordenDTO);
        if (ordenDTO.getOperacion().equals("VENTA")) {
            Integer cantidadDisponible = externalService.getClientesAccion(ordenDTO).getCantidadActual();
            if (cantidadDisponible == null || cantidadDisponible < ordenDTO.getCantidad()) return false;
        }

        return (
            ordenDTO.getCantidad() > 0 &&
            horarioPermitido(ordenDTO.getModo(), ordenDTO.getFechaOperacion()) &&
            externalService.clientAndAccionExists(ordenDTO.getCliente(), ordenDTO.getAccionId())
        );
    }

    private boolean verificarCliente(Integer id) {
        return true;
    }

    private boolean veificarAccionID(Integer id) {
        return true;
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

    private boolean esHorarioTransacciones(ZonedDateTime fechaOperacion) {
        // Lógica para verificar si estamos dentro del horario permitido
        // Implementa según tus necesidades específicas
        Integer inicio = 9;
        Integer fin = 18;
        return fechaOperacion.getHour() > inicio && fechaOperacion.getHour() < fin;
    }

    private void ejecutarOperacion(OrdenDTO ordenDTO) {
        log.info(
            "---------------------------------------------------------------------------------------------------------------orden procesada (en analisis): {}",
            ordenDTO
        );
    }

    private void almacenarResultado(OrdenDTO ordenDTO, boolean exitoso) {
        // Lógica para almacenar el resultado en la base de datos local
        // Puedes actualizar el estado de la orden en la base de datos, por ejemplo
        if (exitoso) {
            ordenesExitosas.add(ordenDTO);
        } else {
            ordenesFallidas.add(ordenDTO);
        }
    }
}
