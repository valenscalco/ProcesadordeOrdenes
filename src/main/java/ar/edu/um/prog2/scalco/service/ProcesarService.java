package ar.edu.um.prog2.scalco.service;

import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import ar.edu.um.prog2.scalco.service.impl.OrdenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ProcesarService {

    private final AnalisisService analisisService;
    private final Logger log = LoggerFactory.getLogger(OrdenServiceImpl.class);

    public ProcesarService(AnalisisService analisisService) {
        this.analisisService = analisisService;
    }

    //@Scheduled(cron = "* * 9 * * ?")
    @Scheduled(cron = "1 * * * * ?")
    public void procesarOrdenesInicioDia() {
        if (analisisService.ordenesExitosas == null) return;
        log.info("TRATANDO------------------: {}");
        for (OrdenDTO ordenDTO : analisisService.ordenesExitosas) {
            if (ordenDTO.getModo().equals("PRINCIPIODIA")) {
                log.info("orden procesada: {}", ordenDTO);
            }
        }
    }

    @Scheduled(cron = "1 * * * * ?")
    public void procesarOrdenesFinDia() {
        if (analisisService.ordenesExitosas == null) return;
        log.info("TRATANDO FINDIA------------------: {}");
        for (OrdenDTO ordenDTO : analisisService.ordenesExitosas) {
            if (ordenDTO.getModo().equals("FINDIA")) {
                log.info("orden procesada: {}", ordenDTO);
            }
        }
    }
}
