package tn.esprit.se.pispring.Service.ProductionService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.CommandRepository;
import tn.esprit.se.pispring.Repository.ProductionRepository;
import tn.esprit.se.pispring.entities.Production;
import tn.esprit.se.pispring.entities.ProductionStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ProductionServiceImpl implements IProductionService {
    private final CommandRepository commandRepository;
    private final ProductionRepository productionRepository;


//CRUD

    @Override
    public List<Production> getAllProductions() {
        return productionRepository.findAll();
    }

    @Override
    public Production getProductionById(Long productionId) {
        return productionRepository.findById(productionId)
                .orElseThrow(() -> new RuntimeException("Production not found"));
    }

    @Override
    public Production addProduction(Production production) {
        return productionRepository.save(production);
    }


    @Override
    public Production updateProduction(Production production) {
            return productionRepository.save(production);
    }

    @Override
    public void deleteProduction(Long productionId) {
        productionRepository.deleteById(productionId);
    }


    @Override
    public int calculateTotalProductionTime(Production production) {
        if (production.getStartDate() != null && production.getEndDate() != null) {
            Date startDate = new Date(production.getStartDate().getTime());
            Date endDate = new Date(production.getEndDate().getTime());
            long differenceInMillis = endDate.getTime() - startDate.getTime();
            int days = (int) (differenceInMillis / (1000 * 60 * 60 * 24));
            return days;
        } else {
            return 0;
        }
    }



    //Mesure du pourcentage de produits finis sans défaut par rapport au nombre total
    @Override
    public double calculateYieldRate(Production production) {
        int totalProducts = production.getTotalProducts();
        int defectiveProducts = production.getDefectiveProducts();
        if (totalProducts == 0) {
            return 0.0;
        }
        return (double) (totalProducts - defectiveProducts) * 100 / totalProducts;
    }


    @Override
    public double calculateTotalMachineMaintenanceCost(List<Production> productions) {
        double totalCost = 0;
        for (Production production : productions) {
            totalCost += production.getMachineMaintenanceCost();
        }
        return totalCost;
    }
    @Override
    public List<Production> findProductionsWithMostDefectiveProducts() {
        return productionRepository.findTop5ByOrderByDefectiveProductsDesc();
    }

/////////////////////////////////////////////////////////
    //SOURCE: https://www.leanproduction.com/oee/
@Override //ok
public double calculateQuality(Production production) {
    int totalProducts = production.getTotalProducts();
    int defectiveProducts = production.getDefectiveProducts();
    if (totalProducts == 0) {
        return 0; // éviter une division par zéro
    }
    return (double) (totalProducts - defectiveProducts) / totalProducts;
}

@Override
    public long calculateTotalProductionTimeDays(Production production) {
        long totalProductionTimeDays = 0;
        if (production.getStartDate() != null && production.getEndDate() != null) {
            long totalProductionTimeMilliseconds = production.getEndDate().getTime() - production.getStartDate().getTime();
            totalProductionTimeDays = totalProductionTimeMilliseconds / (24 * 60 * 60 * 1000); // Convertir les millisecondes en jours
        }
        return totalProductionTimeDays;
    }

    @Override   //Ok mais ylzm condition sur stoppage date
    public double calculateAvailability(Production production) {
        long plannedProductionTimeDay = calculateTotalProductionTimeDays(production);
        long downtimeMilliseconds = production.getProductionStoppage(); // Convertir le temps d'arrêt de jours en millisecondes
        return (double) (plannedProductionTimeDay - downtimeMilliseconds) / plannedProductionTimeDay;
    }
//Calcul de performance source link
//https://www.spectraltms.com/blog/calcul-du-trs-d%C3%A9finitions-formules-et-exemples#:~:text=La%20fa%C3%A7on%20la%20plus%20simple,)%20%2F%20Temps%20de%20production%20planifi%C3%A9.

    @Override
    public double calculatePerformance(Production production) {
        // Temps de cycle idéal (en millisecondes)
        double idealCycleTime = calculateIdealCycleTime(production);

        // Temps d'exécution (en millisecondes)
        double executionTime = calculateExecutionTime(production);

        // Nombre total de pièces
        int totalPieces = production.getTotalProducts();

        // Calcul de la performance
        double performance = (idealCycleTime * totalPieces) / executionTime;

        return performance;
    }

    // Méthode pour calculer le temps de cycle idéal
    private double calculateIdealCycleTime(Production production) {
        // Vous devez définir le temps de cycle idéal pour votre processus de fabrication.
        // Ce temps est le temps de cycle le plus rapide que votre processus peut atteindre
        // dans des conditions optimales.
        // Par exemple, si vous savez que le temps de cycle idéal est de 900 secondes (en millisecondes),
        // vous pouvez le retourner ici.
        double idealCycleTime = 900000; // 900 secondes en millisecondes
        return idealCycleTime;
    }

    // Méthode pour calculer le temps d'exécution
    private double calculateExecutionTime(Production production) {
        double executionTime = calculateTotalProductionTimeMilliseconds(production);
        return executionTime;
    }

    // Méthode pour calculer le temps de production total (en millisecondes)
    private long calculateTotalProductionTimeMilliseconds(Production production) {
        if (production.getStartDate() != null && production.getEndDate() != null) {
            return production.getEndDate().getTime() - production.getStartDate().getTime();
        }
        return 0;
    }
    @Override
    public double calculateOverallEquipmentEffectiveness(Production production) {
        double availability = calculateAvailability(production);
        double performance = calculatePerformance(production);
        double quality = calculateQuality(production);
        double oee = availability * performance * quality;
        return oee * 100; // Convertir en pourcentage
    }

    @Override
    public double calculateTotalProductionCost(Production production) {
        double totalCost = production.getLaborCost() + production.getRawMaterialCost() + production.getMachineMaintenanceCost();
        return totalCost;
    }
    //Coût de revient par produit ou bien l'indice de productivité
    @Override //KPI  ne9s controlleur
    public double calculateCostPerProduct(Production production) {
        // Somme des coûts liés au cycle de production
        double totalProductionCost = calculateTotalProductionCost(production);

        // Nombre total de produits fabriqués
        int totalProducts = production.getTotalProducts();

        // Calcul du coût de revient par produit
        double costPerProduct = totalProductionCost / totalProducts;

        return costPerProduct;
    }
//KPI pourcentage  Défauts de fabrication
    @Override
    public double calculateScrapRate(Production production) {
        int totalProducts = production.getTotalProducts();
        int defectiveProducts = production.getDefectiveProducts();
        if (totalProducts == 0) {
            return 0.0; // éviter une division par zéro
        }
        return (double) defectiveProducts / totalProducts;
    }






/////////////////////////////////////////////////////////////////////////////////////////

    public int calculateProductionEnCours(Date startDate, Date endDate) {
        List<Production> productionsEnCours = productionRepository.findByProductionStatusAndStartDateBeforeAndEndDateAfter(
                ProductionStatus.EN_COURS, endDate, startDate);
        int totalProductionsEnCours = productionsEnCours.size();

        return totalProductionsEnCours;
    }
    public int calculateProductionRéalisé(Date startDate, Date endDate) {
        List<Production> productionsRéalisés = productionRepository.findByProductionStatusAndStartDateBeforeAndEndDateAfter(
                ProductionStatus.TERMINE, endDate, startDate);
        int totalProductionsEnCours = productionsRéalisés.size();

        return totalProductionsEnCours;
    }
    @Override //KPI Densité des files d’attentes %
    public double calculateQueueDensityPercentage(Date startDate, Date endDate) {
        int productionInQueue = calculateProductionEnCours(startDate,endDate);
        int totalProduction = calculateProductionRéalisé(startDate, endDate);
        // Calculez le ratio de densité des files d'attente en pourcentage
        double queueDensityPercentage = ((double) productionInQueue / totalProduction) * 100;

        return queueDensityPercentage;
    }

    @Override
    public Optional<Production> updateProductionStatus(Long productionId, ProductionStatus newStatus) {
        Optional<Production> optionalProduction = productionRepository.findById(productionId);
        if (optionalProduction.isPresent()) {
            Production production = optionalProduction.get();
            production.setProductionStatus(newStatus);
            return Optional.of(productionRepository.save(production));
        }
        return Optional.empty();
    }

}













//    public void calculateProductionStats(Production production) {
//        long totalProductionTime = calculateTotalProductionTime(production);
//        double yieldRate = calculateYieldRate(production);
//        production.setTotalProductionTime(totalProductionTime);
//        production.setYieldRate(yieldRate);
//        productionRepository.save(production);
//    }