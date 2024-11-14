package tn.esprit.se.pispring.Service.ProductionService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    private final ProductionRepository productionRepository;




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


@Override
public double calculateQuality(Production production) {
    int totalProducts = production.getTotalProducts();
    int defectiveProducts = production.getDefectiveProducts();
    if (totalProducts == 0) {
        return 0;
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

    @Override
    public double calculateAvailability(Production production) {
        long plannedProductionTimeDay = calculateTotalProductionTimeDays(production);
        long downtimeMilliseconds = production.getProductionStoppage(); // Convertir le temps d'arrêt de jours en millisecondes
        return (double) (plannedProductionTimeDay - downtimeMilliseconds) / plannedProductionTimeDay;
    }


    @Override
    public double calculatePerformance(Production production) {

        double idealCycleTime = calculateIdealCycleTime(production);


        double executionTime = calculateExecutionTime(production);


        int totalPieces = production.getTotalProducts();


        double performance = (idealCycleTime * totalPieces) / executionTime;

        return performance;
    }


    private double calculateIdealCycleTime(Production production) {

        double idealCycleTime = 900000;
        return idealCycleTime;
    }


    private double calculateExecutionTime(Production production) {
        double executionTime = calculateTotalProductionTimeMilliseconds(production);
        return executionTime;
    }


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
        return oee * 100;
    }

    @Override
    public double calculateTotalProductionCost(Production production) {
        double totalCost = production.getLaborCost() + production.getRawMaterialCost() + production.getMachineMaintenanceCost();
        return totalCost;
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













