package model;

import java.time.LocalDateTime;

public interface Repeatability {

    LocalDateTime nextTime(LocalDateTime currentDateTime);
    String toString();
}
