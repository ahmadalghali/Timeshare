package uk.ac.gre.aa5119a.timelearn.web.response;

import uk.ac.gre.aa5119a.timelearn.model.notification.NotificationClassBooking;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.model.learn.ClassBooking;

public class NotificationClassBookingResponse {

        private NotificationClassBooking notificationClassBooking;
        private User student;
        private User teacher;
        private ClassBooking classBooking;

        public NotificationClassBookingResponse(NotificationClassBooking notificationClassBooking, User student, User teacher, ClassBooking classBooking) {
            this.notificationClassBooking = notificationClassBooking;
            this.student = student;
            this.teacher = teacher;
            this.classBooking = classBooking;
        }

        public NotificationClassBooking getNotificationClassBooking() {
            return notificationClassBooking;
        }

        public User getStudent() {
            return student;
        }

        public User getTeacher() {
            return teacher;
        }

        public ClassBooking getClassBooking() {
            return classBooking;
        }
    }

