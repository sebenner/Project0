

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//Many-to-Many since each person can have multiple accounts, and each account
//can be used by multiple customers due to joint accounts
@AllArgsConstructor
@Getter
@Setter
public class CustomerAccount {
	private String username;
	private int accountId;
	
	
	@Override
	public int hashCode() {
		return username.hashCode()+accountId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CustomerAccount) {
			CustomerAccount e = (CustomerAccount) obj;
			return this.username == e.getUsername() && this.accountId == e.getAccountId();
		}
		else {
			System.out.println("Test");
			return false;
		}
	}
}