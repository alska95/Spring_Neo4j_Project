package com.example.neo4j.person.controller;


import com.example.neo4j.person.dto.PersonDto;
import com.example.neo4j.person.entity.Person;
import com.example.neo4j.person.service.PersonService;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonController {

    final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PutMapping("/person")
    public PersonDto updatePerson(@RequestBody PersonDto personDto){
        return personService.updatePerson(personDto);
    }

    @GetMapping("/person/{name}")
    public PersonDto findPerson(@PathVariable String name){
        return personService.findPerson(name);
    }

    @PostMapping("/person")
    public PersonDto createPerson(@RequestBody PersonDto personDto){
        return personService.savePerson(personDto);
    }
}
