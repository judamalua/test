
package utilities;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;

import utilities.internal.ConsoleReader;
import utilities.internal.SchemaPrinter;
import domain.Trip;

public class MainSearch {

	public static void main(final String[] args) throws Throwable {
		MainSearch.fullTextSearch();
	}

	@SuppressWarnings("unchecked")
	public static void fullTextSearch() throws Throwable {
		LogManager.getLogger("org.hibernate").setLevel(Level.OFF);
		final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Acme-Explorer");
		final EntityManager em = entityManagerFactory.createEntityManager();
		final FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);

		MainSearch.createIndexer(fullTextEntityManager);
		for (;;) {
			System.out.println("Inserte las palabras de búsqueda");
			em.getTransaction().begin();

			final ConsoleReader cr = new ConsoleReader();
			final String keyWord = cr.readLine();
			final QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Trip.class).get();
			final org.apache.lucene.search.Query query = qb.keyword().onFields("ticker", "title", "description").matching(keyWord).createQuery();
			final FullTextQuery persistenceQuery = fullTextEntityManager.createFullTextQuery(query, Trip.class);

			final List<Trip> result = persistenceQuery.getResultList();
			em.getTransaction().commit();
			System.out.println(" ========= Objetos encontrados ============= (" + result.size() + ")");
			for (final Trip t : result)
				SchemaPrinter.print(t);
		}

	}

	public static List<Trip> fullTextSearch(final String keyWord) throws Throwable {
		LogManager.getLogger("org.hibernate").setLevel(Level.OFF);
		final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Acme-Explorer");
		final EntityManager em = entityManagerFactory.createEntityManager();
		final FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);

		MainSearch.createIndexer(fullTextEntityManager);
		final QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Trip.class).get();
		final org.apache.lucene.search.Query query = qb.keyword().onFields("ticker", "title", "description").matching(keyWord).createQuery();
		final FullTextQuery persistenceQuery = fullTextEntityManager.createFullTextQuery(query, Trip.class);

		final List<Trip> result = persistenceQuery.getResultList();

		return result;
	}

	public static void createIndexer(final FullTextEntityManager fullTextEntityManager) {
		try {
			fullTextEntityManager.createIndexer(Trip.class).startAndWait();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
