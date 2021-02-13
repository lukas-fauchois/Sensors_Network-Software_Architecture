package com.groupe2.dao;

import java.util.Date;

import com.groupe2.entities.Measure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeasureRepository extends JpaRepository<Measure, Long> {
    @Query("SELECT m FROM Measure m WHERE m.sensor.sensorId=:id AND m.dateTaken>=:d1 AND m.dateTaken<=:d2")
    public Page<Measure> listMeasure(@Param("id")Long sensorId, @Param("d1")Date d1, @Param("d2")Date d2, Pageable pageable);
}
