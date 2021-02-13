package com.groupe2.dao;

import com.groupe2.entities.Sensor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SensorRepository extends JpaRepository<Sensor, Long>{
    @Query("SELECT s FROM Sensor s JOIN s.users u WHERE u.login=:l")
    public Page<Sensor> listSensor(@Param("l")String login, Pageable pageable);
}
