package de.rohmio.util.filelabeling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.rohmio.util.filelabeling.database.ConnectorAccessor;
import de.rohmio.util.filelabeling.database.SQLiteConnector;
import de.rohmio.util.filelabeling.model.Tag;

public class SQLiteTest {

	private static SQLiteConnector conn;
	
	@BeforeAll
	public static void initDatabase() {
		new File(Constants.DataBaseName).delete();
		conn = ConnectorAccessor.Instance().Connector();
	}
	
	@Test
	public void createTagsViaConstructor() {
		Tag tag1 = new Tag("some label", "some category");
		Tag tag2 = new Tag("some label", "some other category");
		Tag tag3 = new Tag("some other label", "some category");
		Tag tag4 = new Tag("some other label", "some other category");
		assertNotNull(tag1.getId());
		assertNotNull(tag2.getId());
		assertNotNull(tag3.getId());
		assertNotNull(tag4.getId());
		
		List<Tag> tags = conn.getTags();
		assertTrue(tags.size() >= 4);

		conn.createFile(new File("test-file"), tag1, tag4);
		conn.createFile(new File("test-file2"));
		
//		ITaggedFile taggedFile = conn.getTaggedFile("test-file");
//		System.out.println(taggedFile);
	}
	
	@Test
	public void createTagsDirectly() {
		int createTag = conn.createTagAndGetId("no new", "some category");
		int createTagSame = conn.createTagAndGetId("no new", "some category");
		assertEquals(createTag, createTagSame);
		int createTag2 = conn.createTagAndGetId("no new 2", "some category");
		assertNotNull(createTag2);
		
		Integer woCat = conn.createTagAndGetId("withough category", null);
		assertNotNull(woCat);

		// Make sure different tags have actually different IDs
		assertNotEquals(createTag,  createTag2);
		assertNotEquals(createTag,  woCat);
		assertNotEquals(createTag2, woCat);
	}
	
}
