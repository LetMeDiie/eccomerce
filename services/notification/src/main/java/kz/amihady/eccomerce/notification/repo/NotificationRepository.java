package kz.amihady.eccomerce.notification.repo;

import kz.amihady.eccomerce.notification.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationRepository extends MongoRepository<Notification, String>{

}
