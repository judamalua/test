
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CategoryRepository;
import domain.Administrator;
import domain.Category;
import domain.Trip;

@Service
@Transactional
public class CategoryService {

	// Managed repository --------------------------------------------------

	@Autowired
	private CategoryRepository	categoryRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private TripService			tripService;


	// Simple CRUD methods --------------------------------------------------

	public Category create() {
		this.actorService.checkUserLogin();

		Category result;
		//		final Collection<Trip> trips = new HashSet<Trip>();
		final Collection<Category> categories = new HashSet<Category>();

		result = new Category();
		//result.setTrips(trips);
		result.setCategories(categories);

		return result;
	}

	public Collection<Category> findAll() {

		Collection<Category> result;

		Assert.notNull(this.categoryRepository);
		result = this.categoryRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Category findOne(final int categoryId) {

		Category result;

		result = this.categoryRepository.findOne(categoryId);

		return result;

	}

	public Category save(final Category category) {
		this.actorService.checkUserLogin();
		this.checkCategory(category);
		Category rootCategory;
		final Collection<Trip> trips;
		if (category.getId() != 0)
			trips = this.tripService.findTripsByCategoryId(category.getId());
		else
			trips = new HashSet<Trip>();

		rootCategory = this.categoryRepository.findRootCategory();

		assert category != null;

		// Comprobación palabaras de spam
		if (this.actorService.findActorByPrincipal() instanceof Administrator)
			this.actorService.checkSpamWords(category.getName());

		if (!category.equals(rootCategory) && category.getFatherCategory().equals(null))
			// Si no tiene categoría padre, ponemos la categoría por defecto CATEGORY como
			// categoría padre.
			category.setFatherCategory(this.categoryRepository.findRootCategory());

		//final Collection<Category> childrenCategories = category.getFatherCategory().getCategories();

		// El nombre de la categoría debe ser único dentro de los hijos de un mismo padre.
		//		for (final Category c : childrenCategories)
		//			if (!c.equals(category))
		//				Assert.isTrue(!category.getName().equals(c.getName()));

		Category result;
		//final Collection<Trip> trips = category.getTrips();

		result = this.categoryRepository.save(category);
		for (final Trip t : trips) {
			t.setCategory(result);
			this.tripService.save(t);
		}

		return result;

	}
	public void delete(final Category category) {
		this.actorService.checkUserLogin();

		assert category != null;
		assert category.getId() != 0;
		final Category fatherCategory = category.getFatherCategory();

		Assert.isTrue(this.categoryRepository.exists(category.getId()));

		//		fatherCategory.getCategories().remove(category);
		//		this.save(fatherCategory);
		// Eliminamos recursivamente todas las categorías hijas de las categorías hija de la categoría a eliminar
		this.deleteChildrenCategories(category, category);

		//		 Al eliminar una categoría, referenciamos los viajes a su categoría padre.
		//		for (final Trip t : trips) {
		//			t.setCategory(fatherCategory);
		//			this.tripService.save(t);
		//		}
		// Al eliminar una categoría, referenciamos sus categorías hijas a su categoría padre.
		//		for (final Category c : categories) {
		//			c.setFatherCategory(category.getFatherCategory());
		//			this.save(c);
		//		}

		//		fatherCategoryCategories.remove(category);
		//		fatherCategory.setCategories(fatherCategoryCategories);
		//		this.save(fatherCategory);
		//
		//		this.categoryRepository.delete(result);

	}
	// Other methods --------------------------------------------------

	private void checkCategory(final Category category) {

		final boolean result;
		final Map<String, Category> mem;

		mem = new HashMap<String, Category>();
		result = this.getRootFather(category, mem);

		Assert.isTrue(result);

	}
	private boolean getRootFather(final Category category, final Map<String, Category> mem) {

		boolean result;
		Category rootCategory;

		rootCategory = this.categoryRepository.findRootCategory();
		Assert.notNull(rootCategory);
		if (!category.equals(rootCategory) && !category.getFatherCategory().getCategories().isEmpty())
			for (final Category brotherCategory : category.getFatherCategory().getCategories())
				if (!brotherCategory.equals(category))
					Assert.isTrue(!brotherCategory.getName().equals(category.getName()));
		mem.put(category.getName(), category);

		if (category.equals(rootCategory) || category.getFatherCategory().equals(rootCategory))
			result = true;
		else if (mem.get(category.getFatherCategory().getName()) != null || mem.keySet().contains(category.getFatherCategory().getName()))
			result = false;
		else
			result = this.getRootFather(category.getFatherCategory(), mem);

		Assert.notNull(result);
		return result;
	}

	/**
	 * 
	 * @return children categories from root category.
	 */
	public Collection<Category> getChildrenFromRoot() {

		Collection<Category> categories;
		Category category;

		category = this.categoryRepository.findRootCategory();
		Assert.notNull(category);

		categories = category.getCategories();
		Assert.notNull(categories);

		return categories;

	}

	/**
	 * 
	 * @return category with name "CATEGORY"
	 */
	public Category getRootCategory() {
		Category category;

		category = this.categoryRepository.findRootCategory();
		Assert.notNull(category);

		return category;
	}

	//	private void reorderTripFromDeletedCategory(final Category category) {
	//		Collection<Category> categories;
	//
	//		categories = category.getCategories();
	//
	//		if (!categories.isEmpty())
	//			for (final Category c : categories) {
	//				if (!c.getTrips().isEmpty()) {
	//					final Collection<Trip> tripsCategory = c.getTrips();
	//					for (final Trip t : tripsCategory)
	//						t.setCategory(category.getFatherCategory());
	//				}
	//				if (!c.getCategories().isEmpty())
	//					this.reorderTripFromDeletedCategory(c);
	//			}
	//	}

	//	/**
	//	 * Recursive algorithm to delete a category's children categories
	//	 * 
	//	 * @param category
	//	 *            to delete its children categories
	//	 * 
	//	 * @author Juanmi
	//	 */
	//	private void deleteChildrenCategories(final Category category) {
	//		Collection<Category> categories;
	//
	//		categories = category.getCategories();
	//
	//		if (!categories.isEmpty())
	//			for (final Category c : category.getCategories()) {
	//				if (!c.getCategories().isEmpty())
	//					this.deleteChildrenCategories(c);
	//				this.delete(c);
	//			}
	//
	//	}

	/**
	 * Recursive algorithm to delete a category's children categories
	 * 
	 * @param category
	 *            to delete its children categories
	 * 
	 * @author Juanmi & Manu &&& AleMagician jajajaj
	 */
	private Category deleteChildrenCategories(final Category category, final Category initialCategory) {
		final Collection<Category> categories;
		Category result;
		Collection<Trip> trips;

		result = category;
		categories = category.getCategories();
		trips = this.tripService.findAllTripsByCategoryId(category.getId());

		if (categories.isEmpty()) {
			for (final Trip t : trips) {
				t.setCategory(initialCategory.getFatherCategory());
				this.tripService.save(t);
			}

			final Category fatherCategory = category.getFatherCategory();
			fatherCategory.getCategories().remove(category);
			this.save(fatherCategory);

			this.categoryRepository.delete(category);

		} else {
			final HashSet<Category> cats = new HashSet<Category>(categories);
			for (final Category c : cats)
				this.deleteChildrenCategories(c, initialCategory);

			categories.clear();
			category.setCategories(new HashSet<Category>());
			result = this.save(category);
			this.deleteChildrenCategories(category, initialCategory);

			//			this.categoryRepository.delete(category);

		}
		return result;
	}
}
