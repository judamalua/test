
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.LoginService;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Actor;
import domain.Auditor;
import domain.Note;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class NoteServiceTest extends AbstractTest {

	@Autowired
	public NoteService		noteService;
	@Autowired
	public TripService		tripService;
	@Autowired
	public ActorService		actorService;
	@Autowired
	public AuditorService	auditorService;


	@Test
	public void testCreate() {
		super.authenticate("auditor1");
		Note note;
		note = this.noteService.create();

		Assert.notNull(note);
		Assert.notNull(note.getMoment());
		Assert.notNull(note.getAuditor());
		Assert.isNull(note.getTrip());
		Assert.isNull(note.getRemark());
		Assert.isNull(note.getReplierManager());
		Assert.isNull(note.getReply());

		super.unauthenticate();
	}

	@Test
	public void testFindAll() {
		super.authenticate("admin1");
		final Collection<Note> notes = this.noteService.findAll();

		Assert.notNull(notes);
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		super.authenticate("admin1");
		final Note note1 = (Note) this.noteService.findAll().toArray()[0];
		final int noteId = note1.getId();

		final Note note = this.noteService.findOne(noteId);
		Assert.notNull(note);
		super.authenticate("admin1");
	}

	@Test
	public void testSave() {
		super.authenticate("auditor1");
		UserAccount userAccount;
		Actor actor;
		Auditor auditor;

		final Note note = this.noteService.create();
		final Trip t = (Trip) this.tripService.findAll().toArray()[0];
		note.setTrip(t);
		note.setRemark("Hola, buen viaje.");

		final Note savedNote = this.noteService.save(note);

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		actor = this.actorService.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(actor);
		Assert.isTrue(actor instanceof Auditor);

		auditor = (Auditor) actor;

		Assert.isTrue(this.noteService.findAll().contains(savedNote));
		Assert.isTrue(t.getNotes().contains(savedNote));
		Assert.isTrue(auditor.getNotes().contains(savedNote));

		super.unauthenticate();
	}

	@Test
	public void findNotesByAuditorID() {
		super.authenticate("auditor1");

		final Collection<Note> result;

		result = this.noteService.findNotesByAuditorID();

		Assert.notNull(result);
		Assert.notEmpty(result);

		super.unauthenticate();
	}
}
