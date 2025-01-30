package org.secfix.scheduler;

import org.secfix.dto.GitHubRepoResponse;
import org.secfix.model.ObservedRepo;
import org.secfix.model.ObservedRepoStatus;
import org.secfix.publisher.NotificationPublisher;
import org.secfix.repository.ObservedRepoRepository;
import org.secfix.service.GitHubApiService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
public class ObservedRepoScheduler {

    private final ObservedRepoRepository repoRepository;
    private final GitHubApiService gitHubApiService;
    private final NotificationPublisher notificationPublisher;

    public ObservedRepoScheduler(ObservedRepoRepository repoRepository, GitHubApiService gitHubApiService, NotificationPublisher notificationPublisher) {
        this.repoRepository = repoRepository;
        this.gitHubApiService = gitHubApiService;
        this.notificationPublisher = notificationPublisher;
    }

    @Scheduled(fixedRateString = "${fetch.repo.interval}")
    public void fetchAndStoreObservedRepos() {
        List<ObservedRepo> activeRepos = repoRepository.findByStatus(ObservedRepoStatus.ACTIVE);
        for (ObservedRepo repo : activeRepos) {
            try {
                GitHubRepoResponse gitHubRepoData = gitHubApiService.getRepoData(repo.getOwner(), repo.getName());
                updateRepoData(repo, gitHubRepoData);
                repoRepository.save(repo);

                notificationPublisher.publishNotification("Repository updated: " + repo.getName() + " (" + repo.getOwner() + ")");
            } catch (Exception e) {
                repo.setStatus(ObservedRepoStatus.INVALID);
                repoRepository.save(repo);

                notificationPublisher.publishNotification("Repository marked as INVALID: " + repo.getName() + " (" + repo.getOwner() + ")");
            }
        }
    }

    private void updateRepoData(ObservedRepo repo, GitHubRepoResponse gitHubRepoData) {
        repo.setStars(gitHubRepoData.getStars());
        repo.setOpenIssues(gitHubRepoData.getOpenIssues());
        repo.setLicense(gitHubRepoData.getLicense());
        repo.setStatus(ObservedRepoStatus.ACTIVE);
    }
}
