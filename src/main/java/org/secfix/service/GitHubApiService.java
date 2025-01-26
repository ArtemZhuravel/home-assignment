package org.secfix.service;

import org.secfix.dto.GitHubRepoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubApiService {

    private static final String GITHUB_API_URL = "https://api.github.com/repos/";

    @Value("${github.token}")
    private String githubToken;

    private final RestTemplate restTemplate;

    public GitHubApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GitHubRepoResponse getRepoData(String owner, String name) {
        String url = GITHUB_API_URL + owner + "/" + name;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GitHubRepoResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubRepoResponse.class);
        return response.getBody();
    }
}
