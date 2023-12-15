package ar.edu.um.prog2.scalco.service;

import ar.edu.um.prog2.scalco.repository.OrdenRepository;
import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import ar.edu.um.prog2.scalco.service.mapper.OrdenMapper;
import ar.edu.um.prog2.scalco.web.rest.OrdenResource;
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

    public void analizarOrden(OrdenDTO ordenDTO) {
        // Verificar si es posible procesar la orden
        if (esPosibleProcesar(ordenDTO)) {
            // Ejecutar la operación de compra o venta
            ejecutarOperacion(ordenDTO);

            // Almacenar el resultado de la orden
            almacenarResultado(ordenDTO, true);
        } else {
            // Almacenar la orden en la lista de órdenes fallidas
            almacenarResultado(ordenDTO, false);
        }
        log.info("lista analisis ordenes validas : {}", ordenesExitosas);
        log.info("lista analisis ordenes no validas : {}", ordenesFallidas);
    }

    private boolean esPosibleProcesar(OrdenDTO ordenDTO) {
        // Lógica para verificar condiciones de procesamiento
        return ordenDTO.getCantidad() > 0 && horarioPermitido(ordenDTO.getModo(), ordenDTO.getFechaOperacion());
    }

    // Método para verificar si una orden es instantánea
    private boolean horarioPermitido(String modo, ZonedDateTime fechaOperacion) {
        // Lógica para verificar el horario permitido según el modo de la orden
        return "AHORA".equals(modo) && esHorarioTransacciones(fechaOperacion);
    }

    private boolean esHorarioTransacciones(ZonedDateTime fechaOperacion) {
        // Lógica para verificar si estamos dentro del horario permitido
        // Implementa según tus necesidades específicas
        Integer inicio = 9;
        Integer fin = 18;
        return fechaOperacion.getHour() > inicio && fechaOperacion.getHour() < fin;
    }

    private void ejecutarOperacion(OrdenDTO ordenDTO) {
        // Lógica para ejecutar operaciones de compra o venta
        // Puedes interactuar con servicios externos o actualizar la base de datos
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
