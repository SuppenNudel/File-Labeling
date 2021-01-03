package de.rohmio.util.filelabeling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.rohmio.util.filelabeling.database.SQLiteConnector;
import de.rohmio.util.filelabeling.model.Tag;

public class SQLiteTest {

	private static SQLiteConnector conn;
	
	@BeforeAll
	public static void initDatabase() {
		new File("test.db").delete();
		conn = new SQLiteConnector("test.db");
	}
	
	@Test
	public void createLabels() {
		Tag tag1 = new Tag("some label", "some category");
		Tag tag2 = new Tag("some label", "some other category");
		Tag tag3 = new Tag("some other label", "some category");
		assertEquals(1, conn.createTagsIfNotExist(tag1));
		assertEquals(2, conn.createTagsIfNotExist(
				tag2,
				tag3));
		assertEquals(0, conn.createTagsIfNotExist(tag1));
		
		List<Tag> tags = conn.getTags();
		assertEquals(3, tags.size());
		System.out.println(tags);
		
		conn.createFile(new File("test-file"), tag2);
	}
	
}
