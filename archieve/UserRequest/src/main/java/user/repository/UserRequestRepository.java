package user.repository;

import org.springframework.data.repository.CrudRepository;

import user.model.UserRequest;

public interface UserRequestRepository extends CrudRepository<UserRequest, Long> {

}
