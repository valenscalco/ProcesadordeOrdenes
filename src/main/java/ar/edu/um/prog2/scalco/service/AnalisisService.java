package ar.edu.um.prog2.scalco.service;

import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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
            log.info("Orden agregada a lista analisis ordenes validas : {}", ordenDTO);
        } else {
            // Almacenar la orden en la lista de órdenes fallidas
            ordenDTO.setOperacionExitosa(false);
            almacenarResultado(ordenDTO, false);
            log.info("Orden agregada a lista analisis ordenes no validas : {}", ordenDTO);
        }
    }

    public boolean esPosibleProcesar(OrdenDTO ordenDTO) throws JsonProcessingException {
        // Lógica para verificar condiciones de procesamiento
        log.info("Verificando validez de la orden: {}", ordenDTO);
        if (ordenDTO.getOperacion().equals("VENTA")) {
            Integer cantidadDisponible = externalService.getClientesAccion(ordenDTO).getCantidadActual();
            if (cantidadDisponible == null || cantidadDisponible < ordenDTO.getCantidad()) {
                log.info("Cantidad de acciones disponible para la venta insuficientes: {}", ordenDTO);
                ordenDTO.setOperacionObservaciones("Cantidad disponible insuficiente para la venta");
                return false;
            }
        }

        return (
            ordenDTO.getCantidad() > 0 &&
            horarioPermitido(ordenDTO.getModo(), ordenDTO.getFechaOperacion()) &&
            clienteAccionValido(ordenDTO.getCliente(), ordenDTO.getAccionId())
        );
        //return (
        //    ordenDTO.getCantidad() > 0 &&
        //        horarioPermitido(ordenDTO.getModo(), ordenDTO.getFechaOperacion(), ordenDTO) &&
        //        clienteAccionValido(ordenDTO.getCliente(), ordenDTO.getAccionId())
        // );
    }

    //public boolean horarioPermitido(String modo, ZonedDateTime fechaOperacion, OrdenDTO ordenDTO)
    public boolean horarioPermitido(String modo, ZonedDateTime fechaOperacion) {
        // Lógica para verificar el horario permitido según el modo de la orden
        if ("AHORA".equals(modo) && esHorarioTransacciones(fechaOperacion)) {
            log.info("Orden dentro del horario permitido");
            return true;
        } else if ("PRINCIPIODIA".equals(modo) || "FINDIA".equals(modo)) {
            log.info("Orden dentro del horario permitido");
            return true;
        }
        log.info("Orden fuera del horario permitido");
        //ordenDTO.setOperacionObservaciones("Orden fuera del horario permitido");
        return false;
    }

    private boolean clienteAccionValido(Integer cliente, Integer accionId) {
        // Lógica para verificar el horario permitido según el modo de la orden
        if (externalService.clientAndAccionExists(cliente, accionId)) {
            log.info("Cliente y accion valido");
            return true;
        }
        log.info("Cliente y/o accion no valido");
        return false;
    }

    private boolean esHorarioTransacciones(ZonedDateTime fechaOperacion) {
        // Lógica para verificar si estamos dentro del horario permitido
        int inicio = 9;
        int fin = 18;
        return fechaOperacion.getHour() >= inicio && fechaOperacion.getHour() <= fin;
    }

    private void ejecutarOperacion(OrdenDTO ordenDTO) throws JsonProcessingException {
        if ("PRINCIPIODIA".equals(ordenDTO.getModo()) || "FINDIA".equals(ordenDTO.getModo())) {
            Float precio;
            precio = externalService.getValorAccion(ordenDTO.getAccion());
            ordenDTO.setPrecio(precio);
            log.info("Ultimo valor de accion verificado");
        }
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

    public List<OrdenDTO> analizarNoProcesadas(String modo) throws JsonProcessingException {
        List<OrdenDTO> recoveredList = ordenService.findAll();

        for (OrdenDTO ordenDTO : recoveredList) {
            if (ordenDTO.getOperacionExitosa() == null) {
                analizarOrden(ordenDTO);
            }
        }

        List<OrdenDTO> processedList = new ArrayList<>();
        for (OrdenDTO ordenDTO : ordenesExitosas) {
            if (ordenDTO.getModo().equals(modo)) {
                log.info("Procesando orden de id: {}", ordenDTO.getId());
                ejecutarOperacion(ordenDTO);
                processedList.add(ordenDTO);
                ordenService.save(ordenDTO);
            }
        }
        if (!processedList.isEmpty()) {
            externalService.sendReport(processedList);
            log.info("Reporte de ordenes exitosas realizado");
        }
        processedList.clear();

        for (OrdenDTO ordenDTO : ordenesFallidas) {
            ordenService.save(ordenDTO);
        }

        if (!ordenesFallidas.isEmpty()) {
            externalService.sendReport(ordenesFallidas);
            log.info("Reporte de ordenes fallidas realizado");
        }

        ordenesFallidas.clear();
        ordenesExitosas.clear();

        recoveredList = ordenService.findAll();
        return recoveredList;
    }
}
