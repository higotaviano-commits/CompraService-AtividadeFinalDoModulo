package tech.ada.tenthirty.ecommerce.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.ada.tenthirty.ecommerce.client.EstoqueClient;
import tech.ada.tenthirty.ecommerce.client.payload.ItemResponse;
import tech.ada.tenthirty.ecommerce.exception.NotFoundException;
import tech.ada.tenthirty.ecommerce.exception.QuantidadeIndisponivelException;
import tech.ada.tenthirty.ecommerce.model.*;
import tech.ada.tenthirty.ecommerce.payload.ItemAdicionadoRequest;
import tech.ada.tenthirty.ecommerce.payload.response.CompraResponse;
import tech.ada.tenthirty.ecommerce.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdicionarProdutosServiceTest {

    @InjectMocks
    private AdicionarProdutosService service;

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UpdateValorTotalCompraService updateValorTotalCompraService;

    @Mock
    private EstoqueClient estoqueClient;

    @BeforeEach
    void setup() {
        ItemResponse estoque = new ItemResponse();
        estoque.setQuantidade(50);

        when(estoqueClient.consultarEstoqueProduto(anyString()))
                .thenReturn(estoque);
    }

    @Test
    void deveCriarNovaCompra() {
        ItemAdicionadoRequest request = new ItemAdicionadoRequest();
        request.setSkuId("123");
        request.setValorUnitario(12.4);
        request.setQuantidade(1);


        CompraResponse response = service.execute(request);


        ArgumentCaptor<Compra> captor = ArgumentCaptor.forClass(Compra.class);

        verify(compraRepository).save(captor.capture());
        verify(itemRepository).save(any(Item.class));
        verify(updateValorTotalCompraService).execute(any(Compra.class));

        Compra compraSalva = captor.getValue();

        assertNotNull(response.getId());
        assertEquals(response.getId(), compraSalva.getIdentificador());
    }

    @Test
    void deveAdicionarItemEmCompraExistente() {

        Compra compra = new Compra();
        compra.setId(1L);
        compra.setIdentificador(UUID.randomUUID().toString());
        compra.setStatusCompra(StatusCompra.PENDENTE);
        compra.setValorTotal(BigDecimal.TEN);
        compra.setDataCompra(LocalDateTime.now());

        when(compraRepository.findByIdentificador(anyString()))
                .thenReturn(Optional.of(compra));

        ItemAdicionadoRequest request = new ItemAdicionadoRequest();
        request.setSkuId("123");
        request.setValorUnitario(12.4);
        request.setQuantidade(1);
        request.setIdCompra(compra.getIdentificador());


        CompraResponse response = service.execute(request);

        verify(itemRepository).save(any(Item.class));
        verify(compraRepository, never()).save(any());
        verify(updateValorTotalCompraService).execute(compra);

        assertEquals(compra.getIdentificador(), response.getId());
    }

    @Test
    void deveLancarExcecaoQuandoCompraNaoExiste() {

        when(compraRepository.findByIdentificador(anyString()))
                .thenReturn(Optional.empty());

        ItemAdicionadoRequest request = new ItemAdicionadoRequest();
        request.setSkuId("123");
        request.setValorUnitario(12.4);
        request.setQuantidade(1);
        request.setIdCompra("inexistente");


        assertThrows(NotFoundException.class,
                () -> service.execute(request));

        verify(itemRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {

        ItemAdicionadoRequest request = new ItemAdicionadoRequest();
        request.setSkuId("123");
        request.setValorUnitario(12.4);
        request.setQuantidade(51); // maior que estoque


        assertThrows(QuantidadeIndisponivelException.class,
                () -> service.execute(request));

        verify(itemRepository, never()).save(any());
    }
}