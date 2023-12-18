package ar.edu.um.prog2.scalco.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.prog2.scalco.IntegrationTest;
import ar.edu.um.prog2.scalco.domain.ClienteAccion;
import ar.edu.um.prog2.scalco.repository.ClienteAccionRepository;
import ar.edu.um.prog2.scalco.service.dto.ClienteAccionDTO;
import ar.edu.um.prog2.scalco.service.mapper.ClienteAccionMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClienteAccionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClienteAccionResourceIT {

    private static final Integer DEFAULT_CLIENTE = 1;
    private static final Integer UPDATED_CLIENTE = 2;

    private static final Integer DEFAULT_ACCION_ID = 1;
    private static final Integer UPDATED_ACCION_ID = 2;

    private static final String DEFAULT_ACCION = "AAAAAAAAAA";
    private static final String UPDATED_ACCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD_ACTUAL = 1;
    private static final Integer UPDATED_CANTIDAD_ACTUAL = 2;

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cliente-accions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClienteAccionRepository clienteAccionRepository;

    @Autowired
    private ClienteAccionMapper clienteAccionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClienteAccionMockMvc;

    private ClienteAccion clienteAccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClienteAccion createEntity(EntityManager em) {
        ClienteAccion clienteAccion = new ClienteAccion()
            .cliente(DEFAULT_CLIENTE)
            .accionId(DEFAULT_ACCION_ID)
            .accion(DEFAULT_ACCION)
            .cantidadActual(DEFAULT_CANTIDAD_ACTUAL)
            .observaciones(DEFAULT_OBSERVACIONES);
        return clienteAccion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClienteAccion createUpdatedEntity(EntityManager em) {
        ClienteAccion clienteAccion = new ClienteAccion()
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .cantidadActual(UPDATED_CANTIDAD_ACTUAL)
            .observaciones(UPDATED_OBSERVACIONES);
        return clienteAccion;
    }

    @BeforeEach
    public void initTest() {
        clienteAccion = createEntity(em);
    }

    @Test
    @Transactional
    void createClienteAccion() throws Exception {
        int databaseSizeBeforeCreate = clienteAccionRepository.findAll().size();
        // Create the ClienteAccion
        ClienteAccionDTO clienteAccionDTO = clienteAccionMapper.toDto(clienteAccion);
        restClienteAccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteAccionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ClienteAccion in the database
        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeCreate + 1);
        ClienteAccion testClienteAccion = clienteAccionList.get(clienteAccionList.size() - 1);
        assertThat(testClienteAccion.getCliente()).isEqualTo(DEFAULT_CLIENTE);
        assertThat(testClienteAccion.getAccionId()).isEqualTo(DEFAULT_ACCION_ID);
        assertThat(testClienteAccion.getAccion()).isEqualTo(DEFAULT_ACCION);
        assertThat(testClienteAccion.getCantidadActual()).isEqualTo(DEFAULT_CANTIDAD_ACTUAL);
        assertThat(testClienteAccion.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void createClienteAccionWithExistingId() throws Exception {
        // Create the ClienteAccion with an existing ID
        clienteAccion.setId(1L);
        ClienteAccionDTO clienteAccionDTO = clienteAccionMapper.toDto(clienteAccion);

        int databaseSizeBeforeCreate = clienteAccionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClienteAccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteAccion in the database
        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClienteIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteAccionRepository.findAll().size();
        // set the field null
        clienteAccion.setCliente(null);

        // Create the ClienteAccion, which fails.
        ClienteAccionDTO clienteAccionDTO = clienteAccionMapper.toDto(clienteAccion);

        restClienteAccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteAccionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteAccionRepository.findAll().size();
        // set the field null
        clienteAccion.setAccionId(null);

        // Create the ClienteAccion, which fails.
        ClienteAccionDTO clienteAccionDTO = clienteAccionMapper.toDto(clienteAccion);

        restClienteAccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteAccionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClienteAccions() throws Exception {
        // Initialize the database
        clienteAccionRepository.saveAndFlush(clienteAccion);

        // Get all the clienteAccionList
        restClienteAccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clienteAccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].cliente").value(hasItem(DEFAULT_CLIENTE)))
            .andExpect(jsonPath("$.[*].accionId").value(hasItem(DEFAULT_ACCION_ID)))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION)))
            .andExpect(jsonPath("$.[*].cantidadActual").value(hasItem(DEFAULT_CANTIDAD_ACTUAL)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @Test
    @Transactional
    void getClienteAccion() throws Exception {
        // Initialize the database
        clienteAccionRepository.saveAndFlush(clienteAccion);

        // Get the clienteAccion
        restClienteAccionMockMvc
            .perform(get(ENTITY_API_URL_ID, clienteAccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clienteAccion.getId().intValue()))
            .andExpect(jsonPath("$.cliente").value(DEFAULT_CLIENTE))
            .andExpect(jsonPath("$.accionId").value(DEFAULT_ACCION_ID))
            .andExpect(jsonPath("$.accion").value(DEFAULT_ACCION))
            .andExpect(jsonPath("$.cantidadActual").value(DEFAULT_CANTIDAD_ACTUAL))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getNonExistingClienteAccion() throws Exception {
        // Get the clienteAccion
        restClienteAccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClienteAccion() throws Exception {
        // Initialize the database
        clienteAccionRepository.saveAndFlush(clienteAccion);

        int databaseSizeBeforeUpdate = clienteAccionRepository.findAll().size();

        // Update the clienteAccion
        ClienteAccion updatedClienteAccion = clienteAccionRepository.findById(clienteAccion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClienteAccion are not directly saved in db
        em.detach(updatedClienteAccion);
        updatedClienteAccion
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .cantidadActual(UPDATED_CANTIDAD_ACTUAL)
            .observaciones(UPDATED_OBSERVACIONES);
        ClienteAccionDTO clienteAccionDTO = clienteAccionMapper.toDto(updatedClienteAccion);

        restClienteAccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteAccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clienteAccionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClienteAccion in the database
        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeUpdate);
        ClienteAccion testClienteAccion = clienteAccionList.get(clienteAccionList.size() - 1);
        assertThat(testClienteAccion.getCliente()).isEqualTo(UPDATED_CLIENTE);
        assertThat(testClienteAccion.getAccionId()).isEqualTo(UPDATED_ACCION_ID);
        assertThat(testClienteAccion.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testClienteAccion.getCantidadActual()).isEqualTo(UPDATED_CANTIDAD_ACTUAL);
        assertThat(testClienteAccion.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void putNonExistingClienteAccion() throws Exception {
        int databaseSizeBeforeUpdate = clienteAccionRepository.findAll().size();
        clienteAccion.setId(longCount.incrementAndGet());

        // Create the ClienteAccion
        ClienteAccionDTO clienteAccionDTO = clienteAccionMapper.toDto(clienteAccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteAccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteAccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clienteAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteAccion in the database
        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClienteAccion() throws Exception {
        int databaseSizeBeforeUpdate = clienteAccionRepository.findAll().size();
        clienteAccion.setId(longCount.incrementAndGet());

        // Create the ClienteAccion
        ClienteAccionDTO clienteAccionDTO = clienteAccionMapper.toDto(clienteAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteAccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clienteAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteAccion in the database
        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClienteAccion() throws Exception {
        int databaseSizeBeforeUpdate = clienteAccionRepository.findAll().size();
        clienteAccion.setId(longCount.incrementAndGet());

        // Create the ClienteAccion
        ClienteAccionDTO clienteAccionDTO = clienteAccionMapper.toDto(clienteAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteAccionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteAccionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClienteAccion in the database
        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClienteAccionWithPatch() throws Exception {
        // Initialize the database
        clienteAccionRepository.saveAndFlush(clienteAccion);

        int databaseSizeBeforeUpdate = clienteAccionRepository.findAll().size();

        // Update the clienteAccion using partial update
        ClienteAccion partialUpdatedClienteAccion = new ClienteAccion();
        partialUpdatedClienteAccion.setId(clienteAccion.getId());

        partialUpdatedClienteAccion.accion(UPDATED_ACCION).cantidadActual(UPDATED_CANTIDAD_ACTUAL).observaciones(UPDATED_OBSERVACIONES);

        restClienteAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClienteAccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClienteAccion))
            )
            .andExpect(status().isOk());

        // Validate the ClienteAccion in the database
        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeUpdate);
        ClienteAccion testClienteAccion = clienteAccionList.get(clienteAccionList.size() - 1);
        assertThat(testClienteAccion.getCliente()).isEqualTo(DEFAULT_CLIENTE);
        assertThat(testClienteAccion.getAccionId()).isEqualTo(DEFAULT_ACCION_ID);
        assertThat(testClienteAccion.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testClienteAccion.getCantidadActual()).isEqualTo(UPDATED_CANTIDAD_ACTUAL);
        assertThat(testClienteAccion.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void fullUpdateClienteAccionWithPatch() throws Exception {
        // Initialize the database
        clienteAccionRepository.saveAndFlush(clienteAccion);

        int databaseSizeBeforeUpdate = clienteAccionRepository.findAll().size();

        // Update the clienteAccion using partial update
        ClienteAccion partialUpdatedClienteAccion = new ClienteAccion();
        partialUpdatedClienteAccion.setId(clienteAccion.getId());

        partialUpdatedClienteAccion
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .cantidadActual(UPDATED_CANTIDAD_ACTUAL)
            .observaciones(UPDATED_OBSERVACIONES);

        restClienteAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClienteAccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClienteAccion))
            )
            .andExpect(status().isOk());

        // Validate the ClienteAccion in the database
        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeUpdate);
        ClienteAccion testClienteAccion = clienteAccionList.get(clienteAccionList.size() - 1);
        assertThat(testClienteAccion.getCliente()).isEqualTo(UPDATED_CLIENTE);
        assertThat(testClienteAccion.getAccionId()).isEqualTo(UPDATED_ACCION_ID);
        assertThat(testClienteAccion.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testClienteAccion.getCantidadActual()).isEqualTo(UPDATED_CANTIDAD_ACTUAL);
        assertThat(testClienteAccion.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void patchNonExistingClienteAccion() throws Exception {
        int databaseSizeBeforeUpdate = clienteAccionRepository.findAll().size();
        clienteAccion.setId(longCount.incrementAndGet());

        // Create the ClienteAccion
        ClienteAccionDTO clienteAccionDTO = clienteAccionMapper.toDto(clienteAccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clienteAccionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clienteAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteAccion in the database
        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClienteAccion() throws Exception {
        int databaseSizeBeforeUpdate = clienteAccionRepository.findAll().size();
        clienteAccion.setId(longCount.incrementAndGet());

        // Create the ClienteAccion
        ClienteAccionDTO clienteAccionDTO = clienteAccionMapper.toDto(clienteAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteAccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clienteAccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteAccion in the database
        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClienteAccion() throws Exception {
        int databaseSizeBeforeUpdate = clienteAccionRepository.findAll().size();
        clienteAccion.setId(longCount.incrementAndGet());

        // Create the ClienteAccion
        ClienteAccionDTO clienteAccionDTO = clienteAccionMapper.toDto(clienteAccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteAccionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clienteAccionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClienteAccion in the database
        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClienteAccion() throws Exception {
        // Initialize the database
        clienteAccionRepository.saveAndFlush(clienteAccion);

        int databaseSizeBeforeDelete = clienteAccionRepository.findAll().size();

        // Delete the clienteAccion
        restClienteAccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, clienteAccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClienteAccion> clienteAccionList = clienteAccionRepository.findAll();
        assertThat(clienteAccionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
