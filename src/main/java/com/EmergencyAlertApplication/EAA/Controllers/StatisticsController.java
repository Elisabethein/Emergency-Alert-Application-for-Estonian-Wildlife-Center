package com.EmergencyAlertApplication.EAA.Controllers;

import com.EmergencyAlertApplication.EAA.DTOs.FiltersDTO;
import com.EmergencyAlertApplication.EAA.DTOs.StatisticsTableDTO;
import com.EmergencyAlertApplication.EAA.Entities.*;
import com.EmergencyAlertApplication.EAA.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final TicketToAnimalTagService ticketToAnimalTagService;
    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(TicketToAnimalTagService ticketToAnimalTagService, StatisticsService statisticsService) {
        this.ticketToAnimalTagService = ticketToAnimalTagService;
        this.statisticsService = statisticsService;
    }

    @PostMapping("/filtered")
    public ResponseEntity<List<StatisticsTableDTO>> getFilteredTickets(@RequestBody FiltersDTO filtersDTO) {
        try {
            String startDateString = filtersDTO.getAjaperioodiAlgus();
            Timestamp startDate = Timestamp.valueOf(startDateString + " 00:00:00");
            String endDateString = filtersDTO.getAjaperioodiLopp();
            Timestamp endDate = Timestamp.valueOf(endDateString + " 23:59:59");
            List<String> upperSpecies = filtersDTO.getLiigigrupp();
            List<String> species = filtersDTO.getLoomaLiik();
            List<String> region = filtersDTO.getPiirkond();
            List<String> resolution = filtersDTO.getLahendus();
            List<String> injury = filtersDTO.getVigastus();
            List<Ticket> filteredTickets = statisticsService.getFilteredTickets(startDate, endDate, upperSpecies, species, region, resolution, injury);
            // Create table DTOs
            List<StatisticsTableDTO> statisticsDTOs = new ArrayList<>();

            for (Ticket filteredTicket : filteredTickets) {
                StatisticsTableDTO dto = new StatisticsTableDTO();

                dto.setDate(filteredTicket.getOpenDate().toLocalDateTime().toLocalDate());
                dto.setUpperSpecies(filteredTicket.getUpperSpecies().getName());
                dto.setSpecies(filteredTicket.getSpecies().getName());
                dto.setRegion(filteredTicket.getRegion().getName());
                dto.setResolution(filteredTicket.getResolution().getName());
                List<String> injuries = ticketToAnimalTagService.getTicketAnimalTags(filteredTicket.getId());
                dto.setInjuries(injuries);

                statisticsDTOs.add(dto);
            }
            return ResponseEntity.ok(statisticsDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}