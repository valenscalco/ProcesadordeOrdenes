package ar.edu.um.prog2.scalco.service;

import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import ar.edu.um.prog2.scalco.service.impl.OrdenServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public void procesarOrdenesInicioDia() throws JsonProcessingException {
        externalService.getAllOrdenes();
        analisisService.analizarNoProcesadas("PRINCIPIODIA");
    }

    @Scheduled(cron = "0 00 18 * * *")
    public void procesarOrdenesFinDia() throws JsonProcessingException {
        externalService.getAllOrdenes();
        analisisService.analizarNoProcesadas("FINDIA");
    }

    @Scheduled(cron = "0 */3 * * * *")
    public void procesarOrdenesAhora() throws JsonProcessingException {
        /*log.info("SI FUNCIONAAAAAA");
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        log.info("Hora actual: " + dateFormat.format(date));*/
        externalService.getAllOrdenes();
        analisisService.analizarNoProcesadas("AHORA");
    }
}
