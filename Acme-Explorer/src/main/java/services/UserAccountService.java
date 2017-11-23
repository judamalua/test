
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccount;
import security.UserAccountRepository;

@Service
@Transactional
public class UserAccountService {

	@Autowired
	private UserAccountRepository	userAccountRepository;


	// Simple CRUD methods ----------------------------------------------------

	public UserAccount create() {
		UserAccount result;

		result = new UserAccount();

		return result;
	}

	public Collection<UserAccount> findAll() {
		Collection<UserAccount> result;

		Assert.notNull(this.userAccountRepository);
		result = this.userAccountRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public UserAccount findOne(final int userAccountId) {
		UserAccount result;

		result = this.userAccountRepository.findOne(userAccountId);

		return result;
	}

	public UserAccount save(final UserAccount userAccount) {
		assert userAccount != null;

		UserAccount result;
		result = this.userAccountRepository.save(userAccount);

		return result;
	}

	public void delete(final UserAccount userAccount) {
		assert userAccount != null;
		assert userAccount.getId() != 0;

		this.userAccountRepository.delete(userAccount);
	}

}
