package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yearup.models.CartItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<CartItem, Integer>
{
    List<CartItem> findByUserId(int userId);

//    I know you didn't ask for this but you're welcome big G
    Optional<CartItem> findByUserIdAndProductId(int userId, int productId);

    void deleteByUserId(int userId);
}
