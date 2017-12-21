
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.NoteRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Auditor;
import domain.Note;
import domain.Trip;

@Service
@Transactional
public class NoteService {

	// Managed repository --------------------------------------------------

	@Autowired
	private NoteRepository	noteRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private TripService		tripService;
	@Autowired
	private ActorService	actorService;
	@Autowired
	private AuditorService	auditorService;


	// Simple CRUD methods --------------------------------------------------

	public Note create() {
		this.checkUserLogin();
		UserAccount userAccount;
		Note result;
		Actor actor;
		Auditor auditor;

		result = new Note();
		result.setMoment(new Date(System.currentTimeMillis() - 1));

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);
		Assert.isTrue(actor instanceof Auditor);

		auditor = (Auditor) actor;
		result.setAuditor(auditor);

		return result;
	}

	public Collection<Note> findAll() {
		this.checkUserLogin();

		Collection<Note> result;

		Assert.notNull(this.noteRepository);
		result = this.noteRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Note findOne(final int noteId) {
		this.checkUserLogin();

		Note result;

		result = this.noteRepository.findOne(noteId);

		Assert.notNull(result);

		return result;

	}

	public Note save(final Note note) {
		this.checkUserLogin();

		Actor actor;
		UserAccount userAccount;
		Auditor auditor;

		assert note != null;
		final Note storedNote;
		//Requirement 33: A note can´t be update once it has been created;
		final int id = note.getId();

		// Comprobación palabras de spam
		if (this.actorService.findActorByPrincipal() instanceof Auditor) {
			this.actorService.checkSpamWords(note.getRemark());
			if (note.getReply() != null)
				this.actorService.checkSpamWords(note.getRemark());
		}

		if (id != 0) {
			storedNote = this.noteRepository.findOne(id);
			Assert.isTrue(storedNote.getRemark().equals(note.getRemark()));
			//Assert.isTrue(storedNote.getMoment().equals(note.getMoment()));
			Assert.isTrue(storedNote.getTrip().equals(note.getTrip()));
			Assert.isTrue(storedNote.getAuditor().equals(note.getAuditor()));
		}

		//note.setMoment(new Date(System.currentTimeMillis() - 1));

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);
		//Assert.isTrue(actor instanceof Auditor || actor instanceof Administrator);

		if (actor instanceof Auditor) {
			auditor = (Auditor) actor;
			Assert.isTrue(note.getAuditor().equals(auditor));
		}

		Note result;
		Trip trip;

		result = this.noteRepository.save(note);

		//Requirement 33: An Auditor can write notes in his trips;
		trip = note.getTrip();
		auditor = note.getAuditor();

		if (trip.getNotes().contains(note))
			trip.getNotes().remove(note);
		if (auditor.getNotes().contains(note))
			auditor.getNotes().remove(note);

		trip.getNotes().add(result);
		auditor.getNotes().add(result);

		this.tripService.save(trip);
		this.auditorService.save(auditor);

		return result;

	}
	//Requirement 33: A note can`t be deleted once it has been created;

	// Other Business Methods --------------------------------------------------

	// Requirement 33: An Auditor can List his notes;
	public Collection<Note> findNotesByAuditorID() {
		this.checkUserLogin();
		final int id = LoginService.getPrincipal().getId();
		final Actor actor = this.actorService.findActorByUserAccountId(id);
		return this.noteRepository.findNotesByAuditorID(actor.getId());

	}

	private void checkUserLogin() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);

	}

}
