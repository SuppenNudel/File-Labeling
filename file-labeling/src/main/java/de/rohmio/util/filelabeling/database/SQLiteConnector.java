package de.rohmio.util.filelabeling.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import de.rohmio.util.filelabeling.model.ITag;
import de.rohmio.util.filelabeling.model.ITaggedFile;
import de.rohmio.util.filelabeling.model.Tag;
import de.rohmio.util.filelabeling.model.TaggedFile;

public class SQLiteConnector {

	private String url;

	public SQLiteConnector(String databaseName) {
		initSQLiteDriver(databaseName);
		createFilesTable();
		createTagsTable();
	}

	private void initSQLiteDriver(String databaseName) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		url = String.format("jdbc:sqlite:"+databaseName);
	}

	private Table<?> table_files = DSL.table("files");
	private Table<?> table_tags = DSL.table("tags");

	private Field<Integer> field_id = DSL.field("id", SQLDataType.INTEGER.identity(true));
	private Field<String> field_file = DSL.field("file", SQLDataType.VARCHAR.nullable(false));
	private Field<String> field_tags = DSL.field("tags", SQLDataType.VARCHAR);
	private Field<String> field_name = DSL.field("name", SQLDataType.VARCHAR.nullable(false));
	private Field<String> field_category = DSL.field("category", SQLDataType.VARCHAR);

	// as callback so the actual method does not have to deal with closing the connection
	private <T> T doOperation(Function<DSLContext, T> function) {
		T result = null;
		try (Connection connection = DriverManager.getConnection(url)) {
			DSLContext context = DSL.using(connection, SQLDialect.SQLITE);
			result = function.apply(context);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void createFilesTable() {
		doOperation(context -> context
				.createTableIfNotExists(table_files)
				.column(field_id)
				.column(field_file)
				.column(field_tags)
				.constraints(DSL.unique(field_file))
				.execute());
	}

	private void createTagsTable() {
		doOperation(context -> context
				.createTableIfNotExists(table_tags)
				.column(field_id)
				.column(field_name)
				.column(field_category)
				.constraints(DSL.unique(field_name, field_category))
				.execute());
	}
	
	public Integer createTagAndGetId(String tagName, String category) {
		Record1<Integer> record = doOperation(context -> {
			return context
			.insertInto(table_tags)
			.set(field_name, tagName)
			.set(field_category, category)
			.onDuplicateKeyIgnore()
			.returningResult(field_id)
			.fetchOne();
		});
		Integer id;
		if(record == null) {
			id = doOperation(context ->
				context.select(field_id)
				.from(table_tags)
				.where(field_name.eq(tagName).and(field_category.eq(category)))
				.fetchOneInto(Integer.class));
		} else {
			id = record.get(field_id);
		}
		return id;
	}
	
	public List<Tag> getTags() {
		return doOperation(context -> {
			return context.selectFrom(table_tags).fetchInto(Tag.class);
		});
	}
	
	public int createFile(File file, ITag... tags) {
		Condition condition = null;
		if(tags.length == 0) {
			condition= DSL.condition(false);
		} else {
			List<ITag> tagList = Arrays.asList(tags);
			List<Condition> conditions = tagList.stream()
					.map(tag -> field_name.eq(tag.getName()).and(field_category.eq(tag.getCategory())))
					.collect(Collectors.toList());
			condition = conditions.get(0);
			for(int i=1; i<conditions.size(); ++i) {
				condition = condition.or(conditions.get(i));
			}
		}
		final Condition finalCondition = condition;
		List<String> tagIds = doOperation(context -> context
			.select(field_id)
			.from(table_tags)
			.where(finalCondition)
			.fetchInto(String.class));
		return doOperation(context -> {
			return context.insertInto(table_files)
					.set(field_file, file.getName())
					.set(field_tags, String.join(",", tagIds))
					.execute();
		});
	}

	public ITaggedFile getTaggedFile(String fileName) {
		return doOperation(context -> context
				.select()
				.from(table_files)
				.where(field_file.eq(fileName))
				.fetchOneInto(TaggedFile.class));
	}

}
