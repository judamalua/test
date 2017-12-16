
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RangerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Curriculum;
import domain.MessageFolder;
import domain.Ranger;
import domain.SocialIdentity;
import domain.Trip;

@Service
@Transactional
public class RangerService {

	// Managed repository --------------------------------------------------
	@Autowired
	private RangerRepository		rangerRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;
	@Autowired
	private MessageFolderService	messageFolderService;
	@Autowired
	private CurriculumService		curriculumService;
	@Autowired
	private TripService				tripService;


	// Simple CRUD methods --------------------------------------------------

	public Ranger create() {

		//final UserAccount userAccount = LoginService.getPrincipal();
		//Assert.notNull(userAccount);
		//Assert.isTrue(userAccount.getAuthorities().contains(Authority.ADMIN));

		//		final UserAccount userAccount = LoginService.getPrincipal();
		//		Assert.notNull(userAccount);
		//		Assert.isTrue(userAccount.getAuthorities().contains(Authority.ADMIN));
		//this.actorService.checkUserLogin();

		Ranger result;
		final Collection<Trip> trips = new HashSet<Trip>();

		result = new Ranger();
		result.setTrips(trips);
		final MessageFolder inbox = this.messageFolderService.create();
		inbox.setIsDefault(true);
		inbox.setMessageFolderFather(null);
		inbox.setName("in box");
		final MessageFolder outbox = this.messageFolderService.create();
		outbox.setIsDefault(true);
		outbox.setMessageFolderFather(null);
		outbox.setName("out box");
		final MessageFolder notificationbox = this.messageFolderService.create();
		notificationbox.setIsDefault(true);
		notificationbox.setMessageFolderFather(null);
		notificationbox.setName("notification box");
		final MessageFolder trashbox = this.messageFolderService.create();
		trashbox.setIsDefault(true);
		trashbox.setMessageFolderFather(null);
		trashbox.setName("trash box");
		final MessageFolder spambox = this.messageFolderService.create();
		spambox.setIsDefault(true);
		spambox.setMessageFolderFather(null);
		spambox.setName("spam box");

		final Collection<MessageFolder> messageFolders = new ArrayList<>();
		messageFolders.add(inbox);
		messageFolders.add(outbox);
		messageFolders.add(trashbox);
		messageFolders.add(spambox);
		messageFolders.add(notificationbox);

		final Collection<MessageFolder> savedMessageFolders = new ArrayList<MessageFolder>();

		for (final MessageFolder mf : messageFolders)
			savedMessageFolders.add(this.messageFolderService.saveDefaultMessageFolder(mf));

		result.setMessageFolders(savedMessageFolders);

		result.setSocialIdentities(new ArrayList<SocialIdentity>());

		result.setIsBanned(false);
		result.setSuspicious(false);

		return result;
	}

	public Collection<Ranger> findAll() {

		Collection<Ranger> result;

		result = this.rangerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Ranger findOne(final int rangerId) {

		Assert.isTrue(rangerId != 0);

		Ranger result;

		result = this.rangerRepository.findOne(rangerId);
		Assert.notNull(result);

		return result;
	}

	public Ranger save(final Ranger ranger) {
		this.actorService.checkUserLogin();
		Assert.notNull(ranger);
		this.actorService.checkMessageFolders(ranger);
		if (ranger.getCurriculum() != null)
			if (!ranger.getCurriculum().equals(this.rangerRepository.findOne(ranger.getId()).getCurriculum())) {
				final Curriculum c = ranger.getCurriculum();
				c.setRanger(ranger);
				this.curriculumService.save(c);
			}
		if (!ranger.getTrips().isEmpty())
			for (final Trip t : ranger.getTrips()) {
				t.setRanger(ranger);
				this.tripService.save(t);
			}
		Ranger result;

		result = this.rangerRepository.save(ranger);

		return result;
	}

	public void delete(final Ranger ranger) {
		this.actorService.checkUserLogin();
		Assert.notNull(ranger);
		Assert.isTrue(ranger.getId() != 0);
		Assert.isTrue(this.rangerRepository.exists(ranger.getId()));
		if (ranger.getCurriculum() != null)
			this.curriculumService.delete(ranger.getCurriculum());
		if (!ranger.getTrips().isEmpty())
			for (final Trip t : ranger.getTrips())
				this.tripService.delete(t);

		this.rangerRepository.delete(ranger);
	}

	// Other business methods --------------------------------------------------

	public String getRatioRangersWithCurriculum() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		//Assert.isTrue(userAccount.getAuthorities().contains(Authority.ADMIN));

		return this.rangerRepository.getRatioRangersWithCurriculum();
	}
	public String getRatioEndorsersRangers() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		//Assert.isTrue(userAccount.getAuthorities().contains(Authority.ADMIN));

		return this.rangerRepository.getRatioEndorsersRangers();
	}
	public String getRatioSuspiciousRangers() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		//Assert.isTrue(userAccount.getAuthorities().contains(Authority.ADMIN));

		return this.rangerRepository.getRatioSuspiciousRangers();
	}

	// Requisito funcional 14.6, query C/4.
	public String getTripsInfoFromRanger() {
		this.actorService.checkUserLogin();

		return this.rangerRepository.getTripsInfoFromRanger();
	}

	public Collection<Ranger> findSuspiciousRngers() {
		this.actorService.checkUserLogin();
		return this.rangerRepository.findSuspiciousRangers();
	}

}
