package org.secfix.controller;

import org.secfix.model.ObservedRepo;
import org.secfix.model.ObservedRepoStatus;
import org.secfix.repository.ObservedRepoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/observed-repos")
public class ObservedRepoController {

    private final ObservedRepoRepository repoRepository;

    public ObservedRepoController(ObservedRepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    @PostMapping
    public ResponseEntity<ObservedRepo> createRepo(@RequestBody ObservedRepo repo) {
        ObservedRepo savedRepo = repoRepository.save(repo);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRepo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObservedRepo> updateRepo(@PathVariable UUID id, @RequestBody ObservedRepo repo) {
        return repoRepository.findById(id)
                .map(existingRepo -> {
                    repo.setId(existingRepo.getId());
                    repoRepository.save(repo);
                    return ResponseEntity.ok(repo);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRepo(@PathVariable UUID id) {
        return repoRepository.findById(id)
                .map(existingRepo -> {
                    existingRepo.setStatus(ObservedRepoStatus.DELETED);
                    repoRepository.save(existingRepo);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObservedRepo> getRepoById(@PathVariable UUID id) {
        return repoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<Page<ObservedRepo>> getRepos(
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ObservedRepoStatus status,
            @RequestParam(required = false) String license,
            Pageable pageable) {

        Page<ObservedRepo> repos = repoRepository.findAll(pageable);
        return ResponseEntity.ok(repos);
    }
}
