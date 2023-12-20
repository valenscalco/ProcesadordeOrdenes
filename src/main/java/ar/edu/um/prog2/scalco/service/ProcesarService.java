package ar.edu.um.prog2.scalco.service;

import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import ar.edu.um.prog2.scalco.service.impl.OrdenServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Scheduled(cron = "7 19 * * * ?")
    //@Scheduled(cron = "1 * * * * ?")
    public void procesarOrdenesInicioDia() throws JsonProcessingException {
        externalService.getAllOrdenes();
        analisisService.analizarNoProcesadas("PRINCIPIODIA");
    }

    //@Scheduled(cron = "* * * * * ?")
    public void procesarOrdenesFinDia() throws JsonProcessingException {
        externalService.getAllOrdenes();
        analisisService.analizarNoProcesadas("FINDIA");
    }

    //@Scheduled(cron = "* 1 * * * ?")
    public void procesarOrdenesAhora() throws JsonProcessingException {
        externalService.getAllOrdenes();
        analisisService.analizarNoProcesadas("AHORA");
    }
}
