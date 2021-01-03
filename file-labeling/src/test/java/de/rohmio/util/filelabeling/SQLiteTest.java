package de.rohmio.util.filelabeling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.rohmio.util.filelabeling.database.SQLiteConnector;
import de.rohmio.util.filelabeling.model.ITaggedFile;
import de.rohmio.util.filelabeling.model.Tag;
import de.Constants;

public class SQLiteTest {

	private static SQLiteConnector conn;
	
	@BeforeAll
	public static void initDatabase() {
		new File(Constants.DataBaseName).delete();
		conn = new SQLiteConnector(Constants.DataBaseName);
	}
	
	@Test
	public void createLabels() {
		Tag tag1 = new Tag("some label", "some category");
		Tag tag2 = new Tag("some label", "some other category");
		Tag tag3 = new Tag("some other label", "some category");
		Tag tag4 = new Tag("some other label", "some other category");
		assertEquals(1, conn.createTagsIfNotExist(tag1));
		assertEquals(3, conn.createTagsIfNotExist(
				tag2,
				tag3,
				tag4));
		assertEquals(0, conn.createTagsIfNotExist(tag1));
		
		List<Tag> tags = conn.getTags();
		assertEquals(4, tags.size());
		System.out.println(tags);

		conn.createFile(new File("test-file"), tag1, tag4);
		conn.createFile(new File("test-file2"));
		
		ITaggedFile taggedFile = conn.getTaggedFile("test-file");
		System.out.println(taggedFile);
	}
	
}
