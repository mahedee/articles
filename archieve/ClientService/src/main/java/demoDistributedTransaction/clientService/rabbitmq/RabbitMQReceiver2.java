//package demoDistributedTransaction.clientService.rabbitmq;
//
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import demoDistributedTransaction.clientService.model.UserRequest;
//import demoDistributedTransaction.clientService.service.CustomerLedgerService;
//
//@Component
//public class RabbitMQReceiver2 {
//	
//	@Autowired
//	private CustomerLedgerService customerLedgerService;
//	
//	//kaaj korte hobe ekhane
//		@RabbitListener(queues = "CLedgerRequest")
//		public void receiveCLedgerRequest(UserRequest userRequest) {
//			System.out.println("Received userRequest:\n(name,transactionType,amount)="+ userRequest.getUserName()+", "+userRequest.getTransactionType()+", "+userRequest.getAmount());
//			boolean eligibility = customerLedgerService.checkEligibility(userRequest);
//			if(eligibility) {
//				customerLedgerService.addToCustomerLedger(userRequest);
//			}
//			else {
//				userRequest.setStatus("aborted");
//				//return rabbitmq CLedger response userRequest where status is "aborted"
//			}
//		}
//		
//
//}
