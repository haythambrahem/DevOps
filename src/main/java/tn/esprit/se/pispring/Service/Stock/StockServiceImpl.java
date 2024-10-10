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

//    @Override
//    public MouvementStock addMvt(MouvementStock mouvementStock) {
//        return mouvementStockRepository.save(mouvementStock);
//    }

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
    // Méthode pour calculer le nombre de mouvements de stock par type

    @Override
    public List<MouvementStock> getMovementsByType(TypeMouvement type) {
        return mouvementStockRepository.findByTypeMouvement(type);
    }
//le stock actuel de chaque produit
    @Override
    @Transactional
    public Long calculateCurrentStock(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            List<MouvementStock> mouvementsStock = product.getMouvementsStock();
            long currentStock = product.getStock(); // Stock initial du produit
            // Parcourir tous les mouvements de stock associés au produit
            for (MouvementStock mouvementStock : mouvementsStock) {
                if (mouvementStock.getTypeMouvement() == TypeMouvement.ENTREE) {
                    // Ajouter la quantité du mouvement d'entrée au stock actuel
                    currentStock += mouvementStock.getQuantite();
                } else if (mouvementStock.getTypeMouvement() == TypeMouvement.SORTIE) {
                    // Soustraire la quantité du mouvement de sortie du stock actuel
                    currentStock -= mouvementStock.getQuantite();
                }
            }
            return currentStock;
        } else {
            // Si le produit n'est pas trouvé
            return null;
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////

    //Valeur du stock moyen	Somme (quantité article x prix unitaire) du stock moyen
    //ne9s logique ama it works
    @Override
    public double calculateAverageStockValue() {
        List<Product> products = productRepository.findAll();
        double totalStockValue = 0.0;
        for (Product product : products) {
            // Obtenir le stock actuel du produit
            Long currentStock = calculateCurrentStock(product.getProductId());
            if (currentStock != null) {
                // Calculer la valeur du stock pour ce produit
                double productStockValue = currentStock * product.getPrice();
                // Ajouter la valeur du stock de ce produit à la somme totale
                totalStockValue += productStockValue;
            }
        }
        return totalStockValue;
    }

    //Stock moyen = (Stock initial + Stock final)/2
    @Override
    public double calculateAverageStockValueForPeriods(Long productId) {
        // Trouver le produit par son ID
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Obtenir le stock initial du produit
            long initialStock = product.getStock();

            // Obtenir le stock final du produit (calculer le stock actuel)
            Long finalStock = calculateCurrentStock(productId);

            // Calculer le stock moyen pour ce produit sur une période de 2
            double averageStock = (initialStock + finalStock) / 2.0;

            return averageStock;
        } else {
            // Gérer le cas où le produit n'est pas trouvé
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

    ///////////////////////////////////////////////////////////






















//Consommation moyenne=	Somme totale des consommations / nombre total des consommations
//@Override //pour tous les produits
//public double calculateAverageConsumption() {
//    // Récupérer tous les mouvements de stock de type "SORTIE"
//    List<MouvementStock> sortieMovements = mouvementStockRepository.findByTypeMouvement(TypeMouvement.SORTIE);
//
//    // Initialiser la somme totale des quantités prélevées et le nombre total des sorties de stock
//    double totalConsumption = 0.0;
//    int totalOutMovements = sortieMovements.size();
//
//    // Calculer la somme totale des quantités prélevées
//    for (MouvementStock movement : sortieMovements) {
//        totalConsumption += movement.getQuantite();
//    }
//
//    // Calculer la consommation moyenne
//    double averageConsumption = totalConsumption / totalOutMovements;
//
//    return averageConsumption;
//}

}
