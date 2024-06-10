package com.work.thecarpark.repostitory;

import com.work.thecarpark.entity.dilers.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealersRepository extends JpaRepository<Dealer, Integer> {
    @Query("SELECT d FROM Dealer d LEFT JOIN FETCH d.persons WHERE d.id = :id")
    Dealer findByIdWithPersons(@Param("id") int id);
    Optional<Dealer> findByTitle(String title);
    Dealer findDealerById(int id);

}
