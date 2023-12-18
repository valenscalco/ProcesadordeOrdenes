package ar.edu.um.prog2.scalco.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.scalco.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClienteAccionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClienteAccionDTO.class);
        ClienteAccionDTO clienteAccionDTO1 = new ClienteAccionDTO();
        clienteAccionDTO1.setId(1L);
        ClienteAccionDTO clienteAccionDTO2 = new ClienteAccionDTO();
        assertThat(clienteAccionDTO1).isNotEqualTo(clienteAccionDTO2);
        clienteAccionDTO2.setId(clienteAccionDTO1.getId());
        assertThat(clienteAccionDTO1).isEqualTo(clienteAccionDTO2);
        clienteAccionDTO2.setId(2L);
        assertThat(clienteAccionDTO1).isNotEqualTo(clienteAccionDTO2);
        clienteAccionDTO1.setId(null);
        assertThat(clienteAccionDTO1).isNotEqualTo(clienteAccionDTO2);
    }
}
