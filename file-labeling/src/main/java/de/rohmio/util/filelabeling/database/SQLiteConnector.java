package de.rohmio.util.filelabeling.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertValuesStep2;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import de.rohmio.util.filelabeling.model.Tag;

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
				.constraint(DSL.unique(field_file))
				.execute());
	}

	private void createTagsTable() {
		doOperation(context -> context
				.createTableIfNotExists(table_tags)
				.column(field_id)
				.column(field_name)
				.column(field_category)
				.constraint(DSL.unique(field_name, field_category))
				.execute());
	}
	
	public int createTagsIfNotExist(Tag... tags) {
		return doOperation(context -> {
			InsertValuesStep2<?, String, String> insert = context
			.insertInto(table_tags)
			.columns(field_name, field_category);
			for(Tag tag : tags) {
				insert.values(tag.getName(), tag.getCategory());
			}
			return insert.onDuplicateKeyIgnore().execute();
		});
	}
	
	public List<Tag> getTags() {
		return doOperation(context -> {
			return context.selectFrom(table_tags).fetchInto(Tag.class);
		});
	}
	
	public int createFile(File file, Tag tag) {
		List<String> tags = doOperation(context -> context.select(field_id)
				.from(table_tags)
				.where(field_name.eq(tag.getName())
						.and(field_category.eq(tag.getCategory())))
				.fetchInto(String.class));
		return doOperation(context -> {
			return context.insertInto(table_files)
					.set(field_file, file.getName())
					.set(field_tags, String.join(",", tags))
					.execute();
		});
	}

}
