package services;

import java.util.List;

import entity.Status;
import repository.StatusRepository;

public class StatusService {

    private final StatusRepository statusRepository = new StatusRepository();

    public List<Status> getAllStatus() {
        return statusRepository.findAllStatus();
    }
}
