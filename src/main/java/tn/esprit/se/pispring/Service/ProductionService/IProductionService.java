package tn.esprit.se.pispring.Service.ProductionService;

import tn.esprit.se.pispring.entities.Production;
import tn.esprit.se.pispring.entities.ProductionStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IProductionService {
 //   void calculateProductionStats(Production production);

    List<Production> getAllProductions();

    Production getProductionById(Long productionId);

    Production addProduction(Production production);


    Production updateProduction(Production production);

    void deleteProduction(Long productionId);


    int calculateTotalProductionTime(Production production);

    double calculateYieldRate(Production production);

    double calculateTotalMachineMaintenanceCost(List<Production> productions);

    List<Production> findProductionsWithMostDefectiveProducts();

    double calculateTotalProductionCost(Production production);

    /////////////////////////////////////////////////////////
        //SOURCE: https://www.leanproduction.com/oee/
    double calculateQuality(Production production);


    long calculateTotalProductionTimeDays(Production production);

    double calculateAvailability(Production production);


    double calculatePerformance(Production production);

    double calculateOverallEquipmentEffectiveness(Production production);

    //Coût de revient par produit
    //KPI
    double calculateCostPerProduct(Production production);

    //KPI Défauts de fabrication
    double calculateScrapRate(Production production);

    //KPI Densité des files d’attentes %
    double calculateQueueDensityPercentage(Date startDate, Date endDate);

    Optional<Production> updateProductionStatus(Long productionId, ProductionStatus newStatus);
}
