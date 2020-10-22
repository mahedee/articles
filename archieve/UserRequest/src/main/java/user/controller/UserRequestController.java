package user.controller;

import java.util.List;

//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import user.model.UserRequest;
import user.service.UserRequestService;

@RestController
public class UserRequestController {
	@Autowired
	private UserRequestService userRequestService;
	
	@RequestMapping(method=RequestMethod.POST, value = "/requestTransaction")
	public void addToUserRequestList(@RequestBody UserRequest userRequest) {
		userRequestService.addToUserRequestList(userRequest);
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/requestTransaction")
	public List<UserRequest> getAllFromUserRequestList(){
		return userRequestService.getAllFromUserRequestList();
	}
	

}
