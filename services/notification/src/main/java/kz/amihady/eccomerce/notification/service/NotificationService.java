package kz.amihady.eccomerce.notification.service;

import kz.amihady.eccomerce.exception.EntityNotFoundException;
import kz.amihady.eccomerce.notification.mapper.NotificationMapper;
import kz.amihady.eccomerce.notification.repo.NotificationRepository;
import kz.amihady.eccomerce.notification.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public NotificationResponse find(String id){
        log.info("Запрос на получение уведомление для Id:"+id);
        var notification = repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Уведомление не найдено."));
        return mapper.fromNotification(notification);
    }
}
