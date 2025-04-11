package kz.amihady.eccomerce.notification.controller;

import kz.amihady.eccomerce.notification.response.NotificationResponse;
import kz.amihady.eccomerce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications") //контроллер для теста
@RequiredArgsConstructor
public class NotificationRestController {
    private final NotificationService service;

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> findById(
            @PathVariable("notificationId") String notificationId){
        return ResponseEntity.ok(service.find(notificationId));
    }
}
