package com.example.neo4j.person.service;

import com.example.neo4j.person.dto.PersonDto;
import com.example.neo4j.person.entity.Person;
import com.example.neo4j.person.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    final PersonRepository personRepository;

    private Person personDtoToPerson(PersonDto personDto) {
        return new Person(
                personDto.getName(),
                personDto.getBorn()
        );
    }

    private PersonDto personToPersonDto(Person person) {
        return new PersonDto(
                person.getName(),
                person.getBorn()
        );
    }

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonDto savePerson(PersonDto personDto) {
        Person save = personRepository.save(personDtoToPerson(personDto));
        return personToPersonDto(save);
    }

    public PersonDto updatePerson(PersonDto personDto) {
        Person firstByName = personRepository.findFirstByName(personDto.getName());
        if (firstByName != null) {
            Person save = personRepository.save(personDtoToPerson(personDto));
            return personToPersonDto(save);
        }
        return null;
    }

    public PersonDto findPerson(String name) {
        Person firstByName = personRepository.findFirstByName(name);
        return personToPersonDto(firstByName);
    }

}
