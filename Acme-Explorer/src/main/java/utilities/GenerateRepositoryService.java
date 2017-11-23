
package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenerateRepositoryService {

	public static void main(final String[] args) {
		final List<String> l = new ArrayList<String>();

		l.add("ObjetoEjemplo");

		GenerateRepositoryService.generateRepositories(l);
		GenerateRepositoryService.generateServices(l);
	}

	// Repositories Generation Methods --------------------------------------------------
	private static void generateRepositories(final List<String> l) {
		for (final String s : l)
			GenerateRepositoryService.generateRepository(s);
	}

	private static void generateRepository(final String s) {
		final String sr = "\r\npackage repositories;\r\n\r\nimport org.springframework.data.jpa.repository." + "JpaRepository;\r\nimport org.springframework.stereotype.Repository;\r\n\r\nimport domain." + s + ";\r\n\r\n@Repository\r\npublic interface "
			+ s + "Repository extends JpaRepository<" + s + ", Integer> {\r\n\r\n}";
		final File file = new File("src//main//java//repositories//" + s + "Repository.java");
		try {
			GenerateRepositoryService.writeFile(file, sr);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	// Service Generation Methods --------------------------------------------------
	private static void generateServices(final List<String> l) {
		for (final String s : l)
			GenerateRepositoryService.generateService(s);
	}

	private static void generateService(final String s) {
		final String sr = "package services;\r\n\r\nimport java.util.Collection;\r\n\r\nimport javax.transaction.Transactional;\r\n\r\nimport org.springf"
			+ "ramework.beans.factory.annotation.Autowired;\r\nimport org.springframework.stereotype.Service;\r\nimport org.springframework.util.Assert;\r\n\r\nimport repositories."
			+ s
			+ "Repository;\r\nimport domain."
			+ s
			+ ";\r\n\r\n@Service\r\n@Transactional\r\npublic class "
			+ s
			+ "Service {\r\n\r\n\t// Managed repository --------------------------------------------------\r\n\r\n\t@Autowired\r\n\tprivate "
			+ s
			+ "Repository\t"
			+ GenerateRepositoryService.toCase(s)
			+ "Repository;\r\n\r\n\r\n\t// Supporting services --------------------------------------------------\r\n\r\n\t// Simple CRUD methods --------------------------------------------------\r\n\r\n\tpublic "
			+ s
			+ " create() {\r\n\t\t"
			+ s
			+ " result;\r\n\r\n\t\tresult = new "
			+ s
			+ "();\r\n\r\n\t\treturn result;\r\n\t}\r\n\r\n\tpublic Collection<"
			+ s
			+ "> findAll() {\r\n\r\n\t\tCollection<"
			+ s
			+ "> result;\r\n\r\n\t\tAssert.notNull(this."
			+ GenerateRepositoryService.toCase(s)
			+ "Repository);\r\n\t\tresult = this."
			+ GenerateRepositoryService.toCase(s)
			+ "Repository.findAll();\r\n\t\tAssert.notNull(result);\r\n\r\n\t\treturn result;\r\n\r\n\t}\r\n\r\n\tpublic "
			+ s
			+ " findOne(final int "
			+ GenerateRepositoryService.toCase(s)
			+ "Id) {\r\n\r\n\t\t"
			+ s
			+ " result;\r\n\r\n\t\tresult = this."
			+ GenerateRepositoryService.toCase(s)
			+ "Repository.findOne("
			+ GenerateRepositoryService.toCase(s)
			+ "Id);\r\n\r\n\t\treturn result;\r\n\r\n\t}\r\n\r\n\tpublic "
			+ s
			+ " save(final "
			+ s
			+ " "
			+ GenerateRepositoryService.toCase(s)
			+ ") {\r\n\r\n\t\tassert "
			+ GenerateRepositoryService.toCase(s)
			+ " != null;\r\n\r\n\t\t"
			+ s
			+ " result;\r\n\r\n\t\tresult = this."
			+ GenerateRepositoryService.toCase(s)
			+ "Repository.save("
			+ GenerateRepositoryService.toCase(s)
			+ ");\r\n\r\n\t\treturn result;\r\n\r\n\t}\r\n\r\n\tpublic void delete(final "
			+ s
			+ " "
			+ GenerateRepositoryService.toCase(s)
			+ ") {\r\n\r\n\t\tassert "
			+ GenerateRepositoryService.toCase(s)
			+ " != null;\r\n\t\tassert "
			+ GenerateRepositoryService.toCase(s)
			+ ".getId() != 0;\r\n\r\n\t\tAssert.isTrue(this."
			+ GenerateRepositoryService.toCase(s)
			+ "Repository.exists("
			+ GenerateRepositoryService.toCase(s)
			+ ".getId()));\r\n\r\n\t\tthis."
			+ GenerateRepositoryService.toCase(s)
			+ "Repository.delete("
			+ GenerateRepositoryService.toCase(s)
			+ ");\r\n\r\n\t}\r\n}\r\n";
		final File file = new File("src//main//java//services//" + s + "Service.java");
		try {
			GenerateRepositoryService.writeFile(file, sr);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	// Auxiliary Methods --------------------------------------------------
	private static String toCase(final String s) {
		final String res = s.substring(0, 1).toLowerCase() + s.substring(1);
		return res;
	}
	private static void writeFile(final File file, final String s) throws IOException {
		file.getParentFile().mkdirs();
		file.delete();
		file.createNewFile();
		final FileWriter fw = new FileWriter(file);
		final BufferedWriter out = new BufferedWriter(fw);
		out.write(s);
		out.newLine();
		out.flush();
		out.close();
	}

}
