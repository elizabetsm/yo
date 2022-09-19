package yo.proj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yo.proj.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // в hql мы обращаемся не к таблице а к сущности, поэтому тутнадо писать класс а не табличку
    @Query("select usr from User usr where usr.userName = :usrName")
    User getUserById(@Param("usrName") String usrName);
}
