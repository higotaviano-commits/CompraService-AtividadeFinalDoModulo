package tech.ada.tenthirty.ecommerce.services;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tech.ada.tenthirty.ecommerce.exception.NotFoundException;
import tech.ada.tenthirty.ecommerce.model.Compra;
import tech.ada.tenthirty.ecommerce.model.Item;
import tech.ada.tenthirty.ecommerce.model.StatusCompra;
import tech.ada.tenthirty.ecommerce.payload.CompraRequest;
import tech.ada.tenthirty.ecommerce.payload.response.CompraResponse;
import tech.ada.tenthirty.ecommerce.queue.ReservarItemEstoqueProducer;
import tech.ada.tenthirty.ecommerce.repository.CompraRepository;
import tech.ada.tenthirty.ecommerce.repository.ItemRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RealizarCompraServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private NotificarClienteService notificarClienteService;
    @Mock
    private ReservarItemEstoqueProducer reservarItemEstoqueProducer;
    @Mock
    private CompraRepository compraRepository;
    @Mock
    private ModificarStatusCompraService modificarStatusCompraService;
    @InjectMocks
    private RealizarCompraService realizarCompraService;




    @Test
    void shouldRealizarCompra(){
        CompraRequest compraRequest = new CompraRequest();
        compraRequest.setCompraId("1234");

        Compra compra = new Compra();
        compra.setId(1L);
        compra.setIdentificador("1234");

        Item item = new Item();
        item.setQuantidadeUnidade(2);
        item.setSku("SKU1");
        item.setValorUnitario(BigDecimal.TEN);


        when(compraRepository.findByIdentificador(compraRequest.getCompraId())).thenReturn(Optional.of(compra));
        when(itemRepository.findByCompraId(compra.getId())).thenReturn(List.of(item));

        CompraResponse compraResponse = realizarCompraService.realizarCompra(compraRequest);

        assertEquals("1234", compraResponse.getId());
        assertEquals(1, compraResponse.getItens().size());

        verify(notificarClienteService).enviarConfirmacaoCompraCliente(any());
    }


    @Test
    void shouldNotRealizeWhenCompraNotFound(){
        CompraRequest compraRequest = new CompraRequest();
        compraRequest.setCompraId("1234");

        when(compraRepository.findByIdentificador(compraRequest.getCompraId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,() -> realizarCompraService.realizarCompra(compraRequest));
    }


    @Test
    void deveAlterarStatusParaPendente() {


        CompraRequest request = new CompraRequest();
        request.setCompraId("123");

        Compra compra = new Compra();
        compra.setId(1L);
        compra.setIdentificador("123");

        Item item = new Item();
        item.setQuantidadeUnidade(2);
        item.setSku("SKU1");
        item.setValorUnitario(BigDecimal.TEN);

        when(compraRepository.findByIdentificador("123"))
                .thenReturn(Optional.of(compra));

        when(itemRepository.findByCompraId(1L))
                .thenReturn(List.of(item));


        realizarCompraService.realizarCompra(request);

        verify(modificarStatusCompraService)
                .execute(compra, StatusCompra.PENDENTE);
    }
}
