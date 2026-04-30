package tech.ada.tenthirty.ecommerce.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.ada.tenthirty.ecommerce.model.Compra;
import tech.ada.tenthirty.ecommerce.model.Item;
import tech.ada.tenthirty.ecommerce.repository.CompraRepository;
import tech.ada.tenthirty.ecommerce.repository.ItemRepository;
import tech.ada.tenthirty.ecommerce.services.UpdateValorTotalCompraService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateValorTotalCompraServiceTest {

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private UpdateValorTotalCompraService service;

    @Test
    void shoulCalculateTotalValue() {


        Compra compra = new Compra();
        compra.setId(1L);

        Item item1 = new Item();
        item1.setValorUnitario(BigDecimal.valueOf(10));
        item1.setQuantidadeUnidade(2);

        Item item2 = new Item();
        item2.setValorUnitario(BigDecimal.valueOf(5));
        item2.setQuantidadeUnidade(3);

        when(itemRepository.findByCompraId(1L))
                .thenReturn(List.of(item1, item2));


        service.execute(compra);


        assertEquals(BigDecimal.valueOf(35.0), compra.getValorTotal());

        verify(compraRepository).save(compra);
    }

    @Test
    void deveSalvarZeroQuandoNaoHouverItens() {

        Compra compra = new Compra();
        compra.setId(1L);

        when(itemRepository.findByCompraId(1L))
                .thenReturn(List.of());

        service.execute(compra);

        assertEquals(BigDecimal.valueOf(0.0), compra.getValorTotal());

        verify(compraRepository).save(compra);
    }
}