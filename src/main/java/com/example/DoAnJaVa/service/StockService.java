package com.example.DoAnJaVa.service;

import com.example.DoAnJaVa.model.Stock;
import com.example.DoAnJaVa.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    public Stock getStockByProduct(Long productId) {
        return stockRepository.findByProductId(productId);
    }

    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public Stock updateStock(Long id, Stock stockDetails) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Stock not found"));

        stock.setQuantity(stockDetails.getQuantity());

        return stockRepository.save(stock);
    }

    public void deleteStock(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Stock not found"));

        stockRepository.delete(stock);
    }
}
