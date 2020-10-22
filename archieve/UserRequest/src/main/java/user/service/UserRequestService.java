package user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import user.model.UserRequest;
import user.repository.UserRequestRepository;


@Service
public class UserRequestService {
	@Autowired
	private UserRequestRepository userRequestRepository;
	
	@Autowired
	private RabbitMQSender rabbitMQConfig;

	
	public void addToUserRequestList(UserRequest userRequest) {
		userRequestRepository.save(userRequest);
		rabbitMQConfig.send(userRequest);	
	}

	public List<UserRequest> getAllFromUserRequestList(){
		List<UserRequest> userRequestList = new ArrayList<UserRequest>();
		userRequestRepository.findAll().forEach(userRequestList::add);
		return userRequestList;
	}
}
