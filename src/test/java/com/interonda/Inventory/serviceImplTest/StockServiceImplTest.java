package com.interonda.Inventory.serviceImplTest;

import com.interonda.Inventory.entity.Deposito;
import com.interonda.Inventory.entity.Producto;
import com.interonda.Inventory.entity.Stock;
import com.interonda.Inventory.dto.StockDTO;
import com.interonda.Inventory.exceptions.DataAccessException;
import com.interonda.Inventory.exceptions.ResourceNotFoundException;
import com.interonda.Inventory.mapper.StockMapper;
import com.interonda.Inventory.repository.StockRepository;
import com.interonda.Inventory.serviceImplTest.impl.StockServiceImpl;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    private static final Long STOCK_ID = 1L;
    private static final Integer CANTIDAD = 10;
    private static final String OPERACION = "Operacion";
    private static final Long PRODUCTO_ID = 1L;
    private static final Long DEPOSITO_ID = 1L;
    private static final String VALIDATION_ERROR_MESSAGE = "Validation failed";
    private static final String DATABASE_ERROR_MESSAGE = "Database error";
    private static final String STOCK_NOT_FOUND_MESSAGE = "Stock no encontrado con el id: " + STOCK_ID;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @Mock
    private Validator validator;

    @Mock
    private Logger logger;

    @InjectMocks
    private StockServiceImpl stockService;

    private StockDTO stockDTO;
    private Stock stock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        stockService = new StockServiceImpl(stockRepository, stockMapper, validator, logger);

        stockDTO = createStockDTO();
        stock = createStock();
    }

    private StockDTO createStockDTO() {
        StockDTO stockDTO = new StockDTO();
        stockDTO.setCantidad(CANTIDAD);
        stockDTO.setId(STOCK_ID);
        stockDTO.setFechaActualizacion(LocalDateTime.now());
        stockDTO.setOperacion(OPERACION);
        stockDTO.setProductoId(PRODUCTO_ID);
        stockDTO.setDepositoId(DEPOSITO_ID);
        return stockDTO;
    }

    private Stock createStock() {
        Stock stock = new Stock();
        stock.setId(STOCK_ID);
        stock.setCantidad(CANTIDAD);
        stock.setFechaActualizacion(LocalDateTime.now());
        stock.setOperacion(OPERACION);

        Producto producto = new Producto();
        producto.setId(PRODUCTO_ID);
        stock.setProducto(producto);

        Deposito deposito = new Deposito();
        deposito.setId(DEPOSITO_ID);
        stock.setDeposito(deposito);

        return stock;
    }

    @Test
    void createStock_ValidationFailure() {
        doThrow(new RuntimeException(VALIDATION_ERROR_MESSAGE)).when(validator).validate(any());

        DataAccessException exception = assertThrows(DataAccessException.class, () -> stockService.createStock(stockDTO));

        assertEquals("Error creando Stock", exception.getMessage());
        verify(logger).error(eq("Error creando Stock"), any(RuntimeException.class));
    }

    @Test
    void createStock_DataAccessException() {
        when(stockMapper.toEntity(any(StockDTO.class))).thenReturn(stock);
        when(stockRepository.save(any(Stock.class))).thenThrow(new RuntimeException(DATABASE_ERROR_MESSAGE));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> stockService.createStock(stockDTO));

        assertEquals("Error creando Stock", exception.getMessage());
        verify(logger).error(eq("Error creando Stock"), any(RuntimeException.class));
    }

    @Test
    void updateStock_Success() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(stock));
        when(stockMapper.toEntity(any(StockDTO.class))).thenReturn(stock);
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(stockMapper.toDto(any(Stock.class))).thenReturn(stockDTO);

        StockDTO result = stockService.updateStock(stockDTO);

        assertNotNull(result);
        assertEquals(stockDTO.getId(), result.getId());
        verify(logger).info("Actualizando Stock con id: {}", stockDTO.getId());
        verify(logger).info("Stock actualizado exitosamente con id: {}", stock.getId());
    }

    @Test
    void updateStock_ResourceNotFound() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> stockService.updateStock(stockDTO));

        assertEquals(STOCK_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(logger).warn(eq("Stock no encontrado: {}"), eq(STOCK_NOT_FOUND_MESSAGE));
    }

    @Test
    void deleteStock_Success() {
        when(stockRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(stockRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> stockService.deleteStock(STOCK_ID));

        verify(logger).info("Eliminando Stock con id: {}", STOCK_ID);
        verify(logger).info("Stock eliminado exitosamente con id: {}", STOCK_ID);
    }

    @Test
    void deleteStock_ResourceNotFound() {
        when(stockRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> stockService.deleteStock(STOCK_ID));

        assertEquals(STOCK_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(logger).warn("Stock no encontrado: {}", STOCK_NOT_FOUND_MESSAGE);
    }

    @Test
    void deleteStock_DataAccessException() {
        when(stockRepository.existsById(anyLong())).thenReturn(true);
        doThrow(new RuntimeException(DATABASE_ERROR_MESSAGE)).when(stockRepository).deleteById(anyLong());

        DataAccessException exception = assertThrows(DataAccessException.class, () -> stockService.deleteStock(STOCK_ID));

        assertEquals("Error eliminando Stock", exception.getMessage());
        verify(logger).error(eq("Error eliminando Stock"), any(RuntimeException.class));
    }

    @Test
    void getStock_Success() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.of(stock));
        when(stockMapper.toDto(any(Stock.class))).thenReturn(stockDTO);

        StockDTO result = stockService.getStock(STOCK_ID);

        assertNotNull(result);
        assertEquals(stockDTO.getId(), result.getId());
        verify(logger).info("Obteniendo Stock con id: {}", STOCK_ID);
    }

    @Test
    void getStock_ResourceNotFound() {
        when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> stockService.getStock(STOCK_ID));

        assertEquals(STOCK_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(logger).warn("Stock no encontrado: {}", STOCK_NOT_FOUND_MESSAGE);
    }

    @Test
    void getStock_DataAccessException() {
        when(stockRepository.findById(anyLong())).thenThrow(new RuntimeException(DATABASE_ERROR_MESSAGE));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> stockService.getStock(STOCK_ID));

        assertEquals("Error obteniendo Stock", exception.getMessage());
        verify(logger).error(eq("Error obteniendo Stock"), any(RuntimeException.class));
    }

    @Test
    void getAllStocks_Success() {
        Page<Stock> stockPage = mock(Page.class);
        when(stockRepository.findAll(any(Pageable.class))).thenReturn(stockPage);
        when(stockPage.map(any())).thenReturn(mock(Page.class));

        Page<StockDTO> result = stockService.getAllStocks(PageRequest.of(0, 10));

        assertNotNull(result);
        verify(logger).info("Obteniendo todos los Stocks con paginación");
    }

    @Test
    void getAllStocks_DataAccessException() {
        when(stockRepository.findAll(any(Pageable.class))).thenThrow(new RuntimeException(DATABASE_ERROR_MESSAGE));

        DataAccessException exception = assertThrows(DataAccessException.class, () -> stockService.getAllStocks(PageRequest.of(0, 10)));

        assertEquals("Error obteniendo todos los Stocks con paginación", exception.getMessage());
        verify(logger).error(eq("Error obteniendo todos los Stocks con paginación"), any(RuntimeException.class));
    }
}