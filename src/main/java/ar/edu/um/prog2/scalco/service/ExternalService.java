package ar.edu.um.prog2.scalco.service;

import ar.edu.um.prog2.scalco.domain.Orden;
import ar.edu.um.prog2.scalco.service.dto.ClienteAccionDTO;
import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import ar.edu.um.prog2.scalco.service.dto.OrdenesDTO;
import ar.edu.um.prog2.scalco.service.impl.OrdenServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalService {

    @Value("${infoUrl.ordenesUrl}")
    private String urlOrdenes;

    @Value("${infoUrl.token}")
    private String token;

    @Value("${infoUrl.ordenesUrlmine}")
    private String ordenesUrlmine;

    @Value("${infoUrl.reporteCLiAcUrl}")
    private String reporteCLiAcUrl;

    @Value("${infoUrl.ultimovalorUrl}")
    private String ultimovalorUrl;

    @Value("${infoUrl.reportarUrl}")
    private String reportarUrl;

    private final OrdenService ordenService;

    public ExternalService(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    private final Logger log = LoggerFactory.getLogger(OrdenServiceImpl.class);

    public List<OrdenDTO> getAllOrdenes() throws JsonProcessingException {
        WebClient webClient = WebClient.builder().baseUrl(urlOrdenes).defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();

        // Make a GET request
        String response = webClient.get().retrieve().bodyToMono(String.class).block();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OrdenesDTO mappedResponse = objectMapper.readValue(response, OrdenesDTO.class);
        List<Orden> ordenes = mappedResponse.getOrdenes();

        List<OrdenDTO> ordenesDTO = new ArrayList<>();

        for (Orden orden : ordenes) {
            OrdenDTO ordenDTO = ordenService.toDTO(orden);
            ordenService.save(ordenDTO);
            ordenesDTO.add(ordenDTO);
        }
        return ordenesDTO;
    }

    public List<OrdenDTO> getOrdenesProcesadas() throws JsonProcessingException {
        WebClient webClient = WebClient.builder().baseUrl(ordenesUrlmine).build();

        // Make a GET request
        String response = webClient.get().retrieve().bodyToMono(String.class).block();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OrdenesDTO mappedResponse = objectMapper.readValue(response, OrdenesDTO.class);
        List<Orden> ordenes = mappedResponse.getOrdenes();

        List<OrdenDTO> ordenesDTO = new ArrayList<>();

        for (Orden orden : ordenes) {
            OrdenDTO ordenDTO = ordenService.toDTO(orden);
            ordenesDTO.add(ordenDTO);
        }
        return ordenesDTO;
    }

    public Boolean clientAndAccionExists(Integer clienteID, Integer accionID) {
        WebClient webClient = WebClient
            .builder()
            .baseUrl(reporteCLiAcUrl + "clienteId=" + clienteID.toString() + "&accionId=" + accionID.toString())
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();

        // Make a GET request
        Integer response = webClient.get().retrieve().toBodilessEntity().block().getStatusCode().value();

        return response == 200;
    }

    public ClienteAccionDTO getClientesAccion(OrdenDTO ordenDTO) throws JsonProcessingException {
        WebClient webClient = WebClient
            .builder()
            .baseUrl(reporteCLiAcUrl + "clienteId=" + ordenDTO.getCliente().toString() + "&accionId=" + ordenDTO.getAccionId().toString())
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();

        // Make a GET request
        String response = webClient.get().retrieve().bodyToMono(String.class).block();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ClienteAccionDTO mappedResponse = objectMapper.readValue(response, ClienteAccionDTO.class);
        return mappedResponse;
    }

    public Float getValorAccion(String accion) throws JsonProcessingException {
        WebClient webClient = WebClient
            .builder()
            .baseUrl(ultimovalorUrl + accion.toString())
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();

        // Make a GET request
        String response = webClient.get().retrieve().bodyToMono(String.class).block();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode ultimoValor = rootNode.get("ultimoValor").get("valor");
        if (ultimoValor != null && ultimoValor.isNumber()) {
            return ultimoValor.floatValue();
        } else {
            throw new RuntimeException("No se pudo obtener el valor último de la acción.");
        }
    }

    public String sendReport(List<OrdenDTO> ordenes) throws JsonProcessingException {
        log.info(
            "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
        );

        WebClient webClient = WebClient
            .builder()
            .baseUrl(reportarUrl) // Replace with your API endpoint
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token) // Replace with your actual access token
            .build();

        List<String> ordenesJSON = new ArrayList();
        for (OrdenDTO orden : ordenes) {
            ordenesJSON.add(orden.toJSONString());
        }
        String ordenesJSONString = "{\"ordenes\":" + ordenesJSON + "}";
        log.info("ordeneeeesss:  {}", ordenesJSONString);

        String resp = webClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(ordenesJSONString))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        log.info(
            "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
        );

        log.info(
            "reporteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee ",
            resp
        );
        return resp;
    }
}
