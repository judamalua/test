
package controllers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.CategoryService;
import services.ConfigurationService;
import services.ManagerService;
import services.SurvivalClassService;
import services.TripService;
import domain.Actor;
import domain.Category;
import domain.Configuration;
import domain.DomainEntity;
import domain.Explorer;
import domain.Manager;
import domain.SurvivalClass;
import domain.Trip;

@Controller
@RequestMapping("/trip")
public class TripController extends AbstractController {

	// Services -------------------------------------------------------
	@Autowired
	TripService				tripService;
	@Autowired
	ActorService			actorService;
	@Autowired
	ConfigurationService	configurationService;
	@Autowired
	SurvivalClassService	survivalClassService;
	@Autowired
	CategoryService			categoryService;
	@Autowired
	ManagerService			managerService;


	// Constructors -----------------------------------------------------------

	public TripController() {
		super();
	}

	// Paging list -------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "page")
	public ModelAndView list(@RequestParam final int page) throws IllegalArgumentException, IllegalAccessException, IOException {

		//SCRIPT ===============================

		final HashMap<Integer, String> idMap = new HashMap<Integer, String>(); //Integer: Id del domain entity, String: Nombre de la clase para referenciarla
		final HashMap<String, Integer> countDomainEntityNames = new HashMap<String, Integer>(); //String: clase , Integer: id actual para generar nuevo nombre
		final HashMap<String, List<Object>> objects = new HashMap<String, List<Object>>(); //Lista de todos los objetos en memoria
		final HashMap<Object, String> datatypes = new HashMap<Object, String>(); //Object: Datatype, String: Su nombre al que se referencia

		final HashMap<String, Integer> countDataTypesName = new HashMap<String, Integer>(); //String clase

		final EntityManager et = Persistence.createEntityManagerFactory("Acme-Explorer").createEntityManager();

		for (final EntityType<?> entity : et.getMetamodel().getEntities()) {

			final String className = entity.getName();
			//Debe ignorar las clases abstractas
			if (!className.equalsIgnoreCase("actor") && !className.equalsIgnoreCase("domainentity")) {
				final Query q = et.createQuery("from " + className + " c");

				for (final Object o : q.getResultList()) {
					if (!objects.containsKey(o.getClass().getName()))
						objects.put(o.getClass().getName(), new ArrayList<Object>());
					objects.get(o.getClass().getName()).add(o);
					final String classN = o.getClass().getName().replaceFirst("domain.", "").replaceFirst("security.", "");
					if (!countDomainEntityNames.containsKey(classN))
						countDomainEntityNames.put(classN, 0);
					countDomainEntityNames.put(classN, countDomainEntityNames.get(classN) + 1);

					final String name = classN + countDomainEntityNames.get(classN);

					if (o instanceof DomainEntity)
						idMap.put(((DomainEntity) o).getId(), name);

				}
			}
		}

		String xml = "<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd\"> \n";
		for (final String clase : objects.keySet()) {
			xml = xml + "\n\n <!-- %%%%%%  " + clase + " %%%%%% --> \n";
			final List<Object> subobjects = objects.get(clase);
			for (final Object o : subobjects) {
				final Integer id = ((DomainEntity) o).getId();
				xml = xml + "<bean id=\"" + idMap.get(id) + "\" class=\"" + o.getClass().getName() + "\"> \n";

				final List<Field> fields = new ArrayList<Field>(Arrays.asList(o.getClass().getDeclaredFields()));

				if (o.getClass().getSuperclass().getName().contains("Actor"))
					fields.addAll(Arrays.asList(o.getClass().getSuperclass().getDeclaredFields()));
				for (final Field field : fields)
					if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
						field.setAccessible(true); // You might want to set modifier to public first.
						Object value = field.get(o);
						if (value != null) {
							if (value instanceof Date) {
								final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
								value = df.format((Date) value);
							}
							if (value instanceof String || value instanceof Date || value instanceof Boolean || value instanceof Double || value instanceof Integer || value instanceof Long)
								xml = xml + "<property name=\"" + field.getName() + "\" value=\"" + value + "\" /> \n";
							else if (value instanceof Collection<?>) {
								xml = xml + "<property name=\"" + field.getName() + "\" > \n";
								xml = xml + "<list> \n";

								final Collection<?> col = (Collection<?>) value;
								for (final Object obColl : col)
									if (obColl instanceof String || obColl instanceof Date || obColl instanceof Boolean || obColl instanceof Double || obColl instanceof Integer || obColl instanceof Long)
										xml = xml + "<value>" + obColl + "</value> \n";
									else if (obColl instanceof Authority)
										xml = xml + "<bean class=\"security.Authority\"> \n <property name=\"authority\" value=\"" + obColl + "\" /> \n </bean>";
									else
										xml = xml + "<ref bean=\"" + idMap.get(((DomainEntity) obColl).getId()) + "\" /> \n";
								xml = xml + "</list> \n";
								xml = xml + "</property> \n";
							} else if (value instanceof DomainEntity)
								xml = xml + "<property name=\"" + field.getName() + "\" ref=\"" + idMap.get(((DomainEntity) value).getId()) + "\" /> \n";
							else {
								//Datatypes
								Integer valAct = countDataTypesName.get(value.getClass().getSimpleName());
								if (valAct == null)
									valAct = 0;
								valAct++;
								countDataTypesName.put(value.getClass().getSimpleName(), valAct);
								final String dtName = value.getClass().getSimpleName() + valAct;
								if (!datatypes.containsKey(value))
									datatypes.put(value, dtName);

								xml = xml + "<property name=\"" + field.getName() + "\" ref=\"" + dtName + "\" /> \n";

							}

						}
					}
				xml = xml + "</bean>\n";
			}

		}

		xml = xml + "\n\n <!-- = = = = = = DATATYPES = = = = = = = --> \n";
		for (final Entry<Object, String> entry : datatypes.entrySet()) {
			xml = xml + "<bean id=\"" + entry.getValue() + "\" class=\"" + entry.getKey().getClass().getName() + "\"> \n";
			for (final Field field : entry.getKey().getClass().getDeclaredFields()) {
				field.setAccessible(true); // You might want to set modifier to public first.
				final Object value = field.get(entry.getKey());
				if (value != null)
					xml = xml + "<property name=\"" + field.getName() + "\" value=\"" + value + "\" /> \n";
			}
			xml = xml + "</bean>\n";
		}
		xml = xml + "</beans>";

		final String path = "C:/Users/corchu/Desktop/populate.xml";
		Files.write(Paths.get(path), xml.getBytes(), StandardOpenOption.CREATE);
		//Hacer bucle para incorporar los datatypes
		System.out.println(xml);

		//FIN DEL SCRIPT ========================

		ModelAndView result;
		Collection<Trip> trips;
		Page<Trip> tripsPage;
		Pageable pageable;
		final Configuration configuration;

		result = new ModelAndView("trip/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getMaxResults());

		tripsPage = this.tripService.findPublicatedTrips(pageable);
		trips = tripsPage.getContent();

		result.addObject("trips", trips);
		result.addObject("pageNum", tripsPage.getTotalPages());
		result.addObject("requestUri", "trip/list.do");

		return result;
	}

	// listing -------------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Trip> trips;
		Page<Trip> tripsPage;
		Pageable pageable;
		final Configuration configuration;

		result = new ModelAndView("trip/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(0, configuration.getMaxResults());

		tripsPage = this.tripService.findPublicatedTrips(pageable);
		trips = tripsPage.getContent();

		result.addObject("trips", trips);
		result.addObject("pageNum", tripsPage.getTotalPages());
		result.addObject("requestUri", "trip/list.do");

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "categoryId")
	public ModelAndView listCategory(@RequestParam final int categoryId) {
		ModelAndView result;
		Collection<Trip> trips;
		Category category;

		result = new ModelAndView("trip/list");
		category = this.categoryService.findOne(categoryId);
		Assert.notNull(category);

		trips = this.tripService.findTrips(category);

		result.addObject("trips", trips);

		return result;
	}

	// Searching --------------------------------------------------------------
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView search(@RequestParam(value = "keyword", defaultValue = "") final String keyword, @RequestParam(value = "startPrice", defaultValue = "0.0") final double startPrice,
		@RequestParam(value = "endPrice", defaultValue = "10000.0") final double endprice, @RequestParam(value = "", defaultValue = "2000/01/01 00:00") final Date startDate,
		@RequestParam(value = "endDate", defaultValue = "2999/01/01 00:00") final Date endDate, @RequestParam(value = "isAnonymous", defaultValue = "0") final int isAnonymous) {
		ModelAndView result;
		Collection<Trip> trips;
		Page<Trip> tripsPage;

		Pageable pageable;
		final Configuration configuration;

		result = new ModelAndView("trip/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(0, configuration.getMaxResults());

		tripsPage = this.tripService.findTripsBySearchParameters(keyword, startPrice, endprice, startDate, endDate, pageable, isAnonymous);
		trips = tripsPage.getContent();

		result.addObject("trips", trips);
		result.addObject("pageNum", tripsPage.getTotalPages());
		result.addObject("requestUri", "trip/list.do");

		return result;
	}

	// Detailing -----------------------------------------------------------------
	@RequestMapping(value = "/detailed-trip", method = RequestMethod.GET, params = {
		"tripId", "anonymous"
	})
	public ModelAndView detailedList(@RequestParam("tripId") final int tripId, @RequestParam("anonymous") final boolean anonymous) {
		ModelAndView result;
		final Trip trip;
		final Random random;
		boolean hasManager;
		boolean hasExplorer;
		final Actor actor;
		final List<SurvivalClass> survivalClasses;
		final List<Boolean> survivalClassesJoinedIndexed = new ArrayList<Boolean>();
		final Explorer explorer;
		try {
			result = new ModelAndView("trip/detailed-trip");
			random = new Random();
			trip = this.tripService.findOne(tripId);
			survivalClasses = new ArrayList<SurvivalClass>(trip.getSurvivalClasses());
			hasManager = false;
			hasExplorer = false;
			//			Assert.isTrue(trip.getPublicationDate().before(new Date()));

			if (anonymous == false) {
				actor = this.actorService.findActorByPrincipal();

				if (actor instanceof Manager && trip.getManagers().contains(actor))
					hasManager = true;
				else if (actor instanceof Explorer && this.tripService.getAcceptedTripsFromExplorerId(actor.getId()).contains(trip)) {
					hasExplorer = true;
					Assert.isTrue(trip.getPublicationDate().before(new Date()));

					explorer = (Explorer) actor;
					// Añade una lista paralela si el explorer esta inscrito o no en esa survivalClass
					for (final SurvivalClass sv : survivalClasses)
						survivalClassesJoinedIndexed.add(explorer.getSurvivalClasses().contains(sv));

					result.addObject("survivalClassesJoinedIndexed", survivalClassesJoinedIndexed);
				} else
					Assert.isTrue(trip.getPublicationDate().before(new Date()));

			} else
				Assert.isTrue(trip.getPublicationDate().before(new Date()));
			result.addObject("survivalClasses", survivalClasses);
			result.addObject("trip", trip);
			if (trip.getSponsorships().size() > 0)
				result.addObject("sponsorship", trip.getSponsorships().toArray()[random.nextInt(trip.getSponsorships().size())]);
			else
				result.addObject("sponsorship", null);

			result.addObject("hasManager", hasManager);
			result.addObject("hasExplorer", hasExplorer);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/index.do");
		}
		return result;

	}
}
