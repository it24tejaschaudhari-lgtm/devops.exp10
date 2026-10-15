package com.taskbox.repository;

import com.taskbox.entity.OtpStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OtpStorageRepository extends JpaRepository<OtpStorage, Long> {

    // explicit JPQL that matches the entity field `emailOrMobile`
    @Query("SELECT o FROM OtpStorage o " +
           "WHERE o.emailOrMobile = :identifier " +
           "  AND o.otpType = :otpType " +
           "  AND o.expiresAt > :now")
    List<OtpStorage> findByIdentifierAndOtpTypeAndNotExpired(
        @Param("identifier") String identifier,
        @Param("otpType") OtpStorage.OtpType otpType,
        @Param("now") LocalDateTime now
    );

    void deleteByExpiresAtBefore(LocalDateTime now);
}

       