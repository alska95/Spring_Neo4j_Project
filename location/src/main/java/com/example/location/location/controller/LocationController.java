package com.example.location.location.controller;

import com.example.location.location.dto.LocationDto;
import com.example.location.location.dto.ResponseLocationDto;
import com.example.location.location.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class LocationController {

    @Autowired
    private LocationService locationService;


    @PostMapping("/location")
    public ResponseLocationDto saveAPI(@RequestBody LocationDto locationDto) {
        log.info("controller loccd = [{}] ",locationDto.getLOC_CD());
        return locationService.save(locationDto);
    }

    @PutMapping("/location")
    public ResponseLocationDto updateAPI(LocationDto locationDto){
        return locationService.update(locationDto);
    }

    @GetMapping("/location/id/{locationId}")
    public void findAPI(@PathVariable String locationId){
        locationService.find(locationId);
    }

    @DeleteMapping("/location/id/{locationId}")
    public void deleteAPI(@PathVariable String locationId){
        locationService.delete(locationId);
    }
}
