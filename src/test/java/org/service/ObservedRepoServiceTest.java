package org.service;

import org.secfix.dto.GitHubRepoResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.secfix.model.ObservedRepo;
import org.secfix.model.ObservedRepoStatus;
import org.secfix.publisher.NotificationPublisher;
import org.secfix.repository.ObservedRepoRepository;
import org.secfix.scheduler.ObservedRepoScheduler;
import org.secfix.service.GitHubApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ObservedRepoServiceTest {

    @MockBean
    private ObservedRepoRepository repoRepository;

    @MockBean
    private GitHubApiService gitHubApiService;

    @MockBean
    private NotificationPublisher notificationPublisher;

    @Autowired
    private ObservedRepoScheduler observedRepoScheduler;

    @Test
    public void testFetchAndStoreObservedRepos() {
        ObservedRepo repo = new ObservedRepo();
        repo.setId(UUID.randomUUID());
        repo.setOwner("nestjs");
        repo.setName("nest");
        repo.setStatus(ObservedRepoStatus.ACTIVE);

        GitHubRepoResponse gitHubRepoResponse = new GitHubRepoResponse();
        gitHubRepoResponse.setStars(100);
        gitHubRepoResponse.setOpenIssues(5);
        gitHubRepoResponse.setLicense("MIT");

        Mockito.when(repoRepository.findByStatus(ObservedRepoStatus.ACTIVE)).thenReturn(Collections.singletonList(repo));
        Mockito.when(gitHubApiService.getRepoData("nestjs", "nest")).thenReturn(gitHubRepoResponse);

        observedRepoScheduler.fetchAndStoreObservedRepos();

        Mockito.verify(notificationPublisher, Mockito.times(1)).publishNotification(Mockito.anyString());
    }
}
