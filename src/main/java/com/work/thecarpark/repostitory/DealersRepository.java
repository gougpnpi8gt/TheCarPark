package com.work.thecarpark.repostitory;

import com.work.thecarpark.entity.dilers.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealersRepository extends JpaRepository<Dealer, Integer> {
}
