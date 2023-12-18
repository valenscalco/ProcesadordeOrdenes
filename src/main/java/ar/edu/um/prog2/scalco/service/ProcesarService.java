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

    //@Scheduled(cron = "* * 9 * * ?")
    @Scheduled(cron = "1 * * * * ?")
    public void procesarOrdenesInicioDia() {
        if (analisisService.ordenesExitosas == null || analisisService.ordenesExitosas.isEmpty()) {
            log.info("TRATANDO------------------: {}");
            return;
        }
        for (OrdenDTO ordenDTO : analisisService.ordenesExitosas) {
            if (ordenDTO.getModo().equals("PRINCIPIODIA")) {
                log.info(
                    "---------------------------------------------------------------------------------------------------------------orden procesada (en iniciodia): {}",
                    ordenDTO
                );
            }
        }
    }

    @Scheduled(cron = "1 * * * * ?")
    public void procesarOrdenesFinDia() throws JsonProcessingException {
        if (analisisService.ordenesExitosas == null || analisisService.ordenesExitosas.isEmpty()) {
            log.info("TRATANDO FINDIA------------------: {}");
            return;
        }
        for (OrdenDTO ordenDTO : analisisService.ordenesExitosas) {
            if (ordenDTO.getModo().equals("FINDIA")) {
                analisisService.analizarOrden(ordenDTO, true);
                log.info(
                    "---------------------------------------------------------------------------------------------------------------orden procesada (en findia): {}",
                    ordenDTO
                );
            }
        }
    }
}
