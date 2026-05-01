package tech.ada.tenthirty.ecommerce.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.ada.tenthirty.ecommerce.model.Compra;
import tech.ada.tenthirty.ecommerce.model.StatusCompra;
import tech.ada.tenthirty.ecommerce.repository.CompraRepository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class ModificarStatusCompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @InjectMocks
    private ModificarStatusCompraService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveAtualizarStatusESalvarCompra() {
        // Arrange
        Compra compra = new Compra();
        StatusCompra novoStatus = StatusCompra.APROVADA;

        // Act
        service.execute(compra, novoStatus);

        // Assert
        assertEquals(novoStatus, compra.getStatusCompra());
        verify(compraRepository).save(compra);
    }
}