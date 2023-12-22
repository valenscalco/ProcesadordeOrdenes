package ar.edu.um.prog2.scalco.service;

import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import ar.edu.um.prog2.scalco.service.impl.OrdenServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ProcesarService {

    private final AnalisisService analisisService;
    private final ExternalService externalService;

    private final Logger log = LoggerFactory.getLogger(OrdenServiceImpl.class);

    public ProcesarService(AnalisisService analisisService, ExternalService externalService) {
        this.analisisService = analisisService;
        this.externalService = externalService;
    }

    @Scheduled(cron = "0 00 09 * * *")
    public List<OrdenDTO> procesarOrdenesInicioDia() throws JsonProcessingException {
        log.info("Procesando ordenes PRINCIPIODIA");
        return analisisService.analizarNoProcesadas("PRINCIPIODIA");
    }

    @Scheduled(cron = "0 00 18 * * *")
    public List<OrdenDTO> procesarOrdenesFinDia() throws JsonProcessingException {
        log.info("Procesando ordenes FINDIA");
        return analisisService.analizarNoProcesadas("FINDIA");
    }

    //@Scheduled(cron = "0 */5 * * * *")
    public List<OrdenDTO> procesarOrdenesAhora() throws JsonProcessingException {
        externalService.getAllOrdenes();
        log.info("Procesando ordenes AHORA");
        return analisisService.analizarNoProcesadas("AHORA");
    }
}
