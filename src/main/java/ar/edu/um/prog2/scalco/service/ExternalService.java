package ar.edu.um.prog2.scalco.service;

import ar.edu.um.prog2.scalco.domain.Orden;
import ar.edu.um.prog2.scalco.service.dto.ClienteAccionDTO;
import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import ar.edu.um.prog2.scalco.service.dto.OrdenesDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
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

    private final OrdenService ordenService;

    public ExternalService(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

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
}
