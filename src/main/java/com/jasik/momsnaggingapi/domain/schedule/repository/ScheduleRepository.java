package com.jasik.momsnaggingapi.domain.schedule.repository;

import com.jasik.momsnaggingapi.domain.schedule.Schedule;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByScheduleDateAndUserId(LocalDate scheduleDate, Long userId);

    // 벌크 연산 시 clearAutomatically(JPA 1차 캐시) 옵션 필요
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from Schedule where id >= :scheduleId and userId = :userId and originalId = :originalId")
    void deleteWithIdAfter(@Param("scheduleId") Long scheduleId, @Param("userId") Long userId,
        @Param("originalId") Long originalId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Schedule set scheduleName = :scheduleName, scheduleTime = :scheduleTime, alarmTime = :alarmTime where id > :scheduleId and userId = :userId and originalId = :originalId")
    void updateWithIdAfter(@Param("scheduleName") String scheduleName,
        @Param("scheduleTime") String scheduleTime, @Param("alarmTime") LocalTime alarmTime,
        @Param("scheduleId") Long scheduleId, @Param("userId") Long userId,
        @Param("originalId") Long originalId);

    List<Schedule> findAllByCategoryId(Long categoryId);

    Optional<Schedule> findByIdAndUserId(Long id, Long userId);
}
