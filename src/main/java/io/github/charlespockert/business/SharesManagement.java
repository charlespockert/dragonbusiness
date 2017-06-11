package io.github.charlespockert.business;

import java.util.UUID;

import com.google.inject.Inject;

public class SharesManagement {

	@Inject
	public SharesManagement() {

	}

	// Shares
	public void buyShares(int companyId, UUID player, int amount) {
		// Check if there are any shares for sale
		// businessRepository.companyGetByEmployeeId(uuid)
	}

	public void sellShares(int companyId, UUID player, int amount) {

	}

}
