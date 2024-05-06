package vivio.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vivio.spring.domain.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);


    Optional<User> findByNameAndPhoneNumberAndBirthDate(String name, String phoneNum, LocalDate birthDate);
}
