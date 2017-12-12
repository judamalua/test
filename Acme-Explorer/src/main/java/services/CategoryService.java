
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
		final Collection<Trip> trips = new HashSet<Trip>();
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
		//this.checkCategory(category);
		Category rootCategory;
		final Collection<Trip> trips;
		trips = this.tripService.findTripsByCategoryId(category.getId());

		rootCategory = this.categoryRepository.findRootCategory();

		assert category != null;
		if (!category.equals(rootCategory) && category.getFatherCategory().equals(null))
			// Si no tiene categor�a padre, ponemos la categor�a por defecto CATEGORY como
			// categor�a padre.
			category.setFatherCategory(this.categoryRepository.findRootCategory());

		//final Collection<Category> childrenCategories = category.getFatherCategory().getCategories();

		// El nombre de la categor�a debe ser �nico dentro de los hijos de un mismo padre.
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
		final Category rootCategory = this.getRootCategory();
		//final Collection<Category> categories = category.getCategories();
		final Collection<Category> fatherCategoryCategories = category.getFatherCategory().getCategories();
		final Collection<Trip> trips = this.tripService.findTripsByCategoryId(category.getId());

		Assert.isTrue(this.categoryRepository.exists(category.getId()));

		// Al eliminar una categor�a, referenciamos los viajes a su categor�a padre.
		//		for (final Trip t : trips) {
		//			t.setCategory(category.getFatherCategory());
		//			this.tripService.save(t);
		//		}

		// Eliminamos recursivamente todas las categor�as hijas de las categor�as hija de la categor�a a eliminar
		this.deleteChildrenCategories(category);

		// Al eliminar una categor�a, referenciamos sus categor�as hijas a su categor�a padre.
		//		for (final Category c : categories) {
		//			c.setFatherCategory(category.getFatherCategory());
		//			this.save(c);
		//		}

		fatherCategoryCategories.remove(category);
		fatherCategory.setCategories(fatherCategoryCategories);
		this.save(fatherCategory);

		this.categoryRepository.delete(category);

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
	 * @author Juanmi & Manu
	 */
	private void deleteChildrenCategories(final Category category) {
		final Collection<Category> categories, categoriesCopy;
		Collection<Trip> trips;

		categories = category.getCategories();
		categoriesCopy = new HashSet<Category>(category.getCategories());
		trips = this.tripService.findTripsByCategoryId(category.getId());

		if (categories.isEmpty()) {
			for (final Trip t : trips) {
				t.setCategory(category.getFatherCategory());
				this.tripService.save(t);
			}
			this.categoryRepository.delete(category);
		} else
			for (final Category c : categoriesCopy) {
				categories.remove(c);
				category.setCategories(categories);
				this.save(category);
				this.deleteChildrenCategories(c);
			}
	}
}
