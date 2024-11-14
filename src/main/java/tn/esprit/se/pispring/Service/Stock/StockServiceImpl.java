package tn.esprit.se.pispring.Service.Stock;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.se.pispring.Repository.MouvementStockRepository;
import tn.esprit.se.pispring.Repository.ProductRepository;
import tn.esprit.se.pispring.Service.Product.ProductServices;
import tn.esprit.se.pispring.entities.MouvementStock;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.TypeMouvement;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class StockServiceImpl implements IStockService {
    private final ProductRepository productRepository;
    private final MouvementStockRepository mouvementStockRepository;
    ProductServices productService;
    public MouvementStock addMvt(MouvementStock mouvementStock, Long productId) {

        Product product = productService.getProductById(productId);
        mouvementStock.setProduct(product);
        return mouvementStockRepository.save(mouvementStock);
    }



    @Override
    public MouvementStock updateMvt(MouvementStock mouvementStock) {
        return mouvementStockRepository.save(mouvementStock);
    }

    @Override
    public List<MouvementStock> getAllMouvements() {
        return mouvementStockRepository.findAll();
    }

    @Override
    public MouvementStock getMouvementById(Long mvtId) {
        return mouvementStockRepository.findById(mvtId)
                .orElseThrow(() -> new EntityNotFoundException("Mouvement not found with ID: " + mvtId));
    }

    @Override
    public void deleteMouvement(Long mvtId) {
        if (mouvementStockRepository.existsById(mvtId)) {
            mouvementStockRepository.deleteById(mvtId);
        } else {
            throw new EntityNotFoundException("Mouvement not found with ID: " + mvtId);
        }
    }

    @Override
    public List<MouvementStock> getMovementsByType(TypeMouvement type) {
        return mouvementStockRepository.findByTypeMouvement(type);
    }
    @Override
    @Transactional
    public Long calculateCurrentStock(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            List<MouvementStock> mouvementsStock = product.getMouvementsStock();
            long currentStock = product.getStock(); // Stock initial du produit
            for (MouvementStock mouvementStock : mouvementsStock) {
                if (mouvementStock.getTypeMouvement() == TypeMouvement.ENTREE) {
                    currentStock += mouvementStock.getQuantite();
                } else if (mouvementStock.getTypeMouvement() == TypeMouvement.SORTIE) {
                    currentStock -= mouvementStock.getQuantite();
                }
            }
            return currentStock;
        } else {
            return null;
        }
    }

    @Override
    public double calculateAverageStockValue() {
        List<Product> products = productRepository.findAll();
        double totalStockValue = 0.0;
        for (Product product : products) {
            Long currentStock = calculateCurrentStock(product.getProductId());
            if (currentStock != null) {
                double productStockValue = currentStock * product.getPrice();
                totalStockValue += productStockValue;
            }
        }
        return totalStockValue;
    }

    @Override
    public double calculateAverageStockValueForPeriods(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            long initialStock = product.getStock();

            Long finalStock = calculateCurrentStock(productId);

            double averageStock = (initialStock + finalStock) / 2.0;

            return averageStock;
        } else {
            throw new EntityNotFoundException("Product not found with ID: " + productId);
        }
}
    @Override
    public double calculateAverageConsumptionForProduct(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            List<MouvementStock> sortieMovements = mouvementStockRepository.findByTypeMouvementAndProduct(TypeMouvement.SORTIE, product);
            double totalConsumption = 0.0;
            int totalOutMovements = sortieMovements.size();
            for (MouvementStock movement : sortieMovements) {
                totalConsumption += movement.getQuantite();
            }
            double averageConsumption = totalConsumption / totalOutMovements;

            return averageConsumption;
        } else {
            throw new EntityNotFoundException("Product not found with ID: " + productId);
        }
    }



}
